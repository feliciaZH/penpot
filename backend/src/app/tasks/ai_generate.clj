;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.tasks.ai-generate
  (:require
   [app.ai.client :as ai]
   [app.db :as db]
   [integrant.core :as ig]))

(defmethod ig/assert-key ::handler
  [_ params]
  (assert (db/pool? (::db/pool params)) "expected valid db pool"))

(defmethod ig/init-key ::handler
  [_ {::db/keys [pool] :as cfg}]
  (fn [{:keys [id props]}]
    (let [{:keys [intent context]} props
          result (ai/generate* (::ai/client cfg) {:intent intent :context context})
          next-props (assoc props :result result)]
      (db/update! pool :task {:props (db/tjson next-props)} {:id id})
      {:status "completed"})))
