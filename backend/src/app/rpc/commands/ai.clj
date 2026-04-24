;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.rpc.commands.ai
  (:require
   [app.ai.client :as ai]
   [app.config :as cf]
   [app.common.exceptions :as ex]
   [app.common.schema :as sm]
   [app.db :as db]
   [app.rpc :as-alias rpc]
   [app.rpc.doc :as-alias doc]
   [app.worker :as wrk]
   [app.util.services :as sv]))

(defn- ensure-ai-enabled! []
  (when-not (contains? cf/flags :ai-assistant)
    (ex/raise :type :restriction
              :code :feature-disabled
              :hint "ai assistant is disabled")))

(def ^:private schema:ai-generate
  [:map {:title "ai-generate"}
   [:context [:map
              [:scope [:enum :team :resource :ops]]
              [:text [:string {:max 8000}]]]]])

(defn- run-sync!
  [cfg intent context]
  (ensure-ai-enabled!)
  (ai/generate* (::ai/client cfg) {:intent intent :context context}))

(sv/defmethod ::ai-generate-team-announcement
  {::doc/added "2.8"
   ::sm/params schema:ai-generate
   ::rpc/auth true}
  [cfg {:keys [context]}]
  {:content (run-sync! cfg :team-announcement context)})

(sv/defmethod ::ai-suggest-resource-tags
  {::doc/added "2.8"
   ::sm/params schema:ai-generate
   ::rpc/auth true}
  [cfg {:keys [context]}]
  (run-sync! cfg :resource-tags context))

(sv/defmethod ::ai-summarize-ops-events
  {::doc/added "2.8"
   ::sm/params schema:ai-generate
   ::rpc/auth true}
  [cfg {:keys [context]}]
  {:summary (run-sync! cfg :ops-summary context)})

(def ^:private schema:create-ai-task
  [:map {:title "create-ai-task"}
   [:intent [:enum :team-announcement :resource-tags :ops-summary]]
   [:context [:map
              [:scope [:enum :team :resource :ops]]
              [:text [:string {:max 8000}]]]]])

(sv/defmethod ::create-ai-task
  {::doc/added "2.8"
   ::sm/params schema:create-ai-task
   ::rpc/auth true}
  [{::db/keys [pool]} {:keys [intent context ::rpc/profile-id]}]
  (ensure-ai-enabled!)
  (let [task-id (wrk/submit! ::db/conn pool
                             ::wrk/task :ai-generate
                             ::wrk/queue :ai
                             ::wrk/params {:intent intent
                                           :context (ai/sanitize-context context)
                                           :profile-id profile-id})]
    {:task-id task-id}))

(def ^:private schema:get-ai-task
  [:map {:title "get-ai-task"}
   [:task-id ::sm/uuid]])

(sv/defmethod ::get-ai-task
  {::doc/added "2.8"
   ::sm/params schema:get-ai-task
   ::rpc/auth true}
  [{::db/keys [pool]} {:keys [task-id]}]
  (ensure-ai-enabled!)
  (if-let [task (db/get* pool :task {:id task-id})]
    (let [props (:props task)
          props (if (db/pgobject? props) (db/decode-transit-pgobject props) props)]
    {:id (:id task)
     :status (keyword (:status task))
     :error (:error task)
     :result (:result props)})
    (ex/raise :type :not-found :code :ai-task-not-found :task-id task-id)))
