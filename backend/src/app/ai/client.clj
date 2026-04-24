;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.ai.client
  (:require
   [app.common.schema :as sm]
   [app.config :as cf]
   [clojure.string :as str]
   [integrant.core :as ig]))

(def ^:private schema:context
  [:map {:title "ai-context"}
   [:scope [:enum :team :resource :ops]]
   [:text :string]])

(defn sanitize-context
  [{:keys [scope text] :as context}]
  (let [max-chars (cf/get :ai-max-context-chars 1200)
        text      (or text "")
        text      (subs text 0 (min max-chars (count text)))
        text      (str/replace text #"[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}" "[redacted-email]")]
    (assoc context :scope (or scope :ops) :text text)))

(defn generate*
  [client {:keys [intent context]}]
  (let [{:keys [text scope]} (sanitize-context context)]
    (case intent
      :team-announcement
      (format "Team update:\n- Summary: %s\n- Action: Review pending invitations and roles." text)

      :resource-tags
      {:tags (->> (str/split (str/lower-case text) #"\W+")
                  (remove str/blank?)
                  (take 6)
                  vec)}

      :ops-summary
      (format "Ops summary (%s): %s" (name scope) text)

      "Unsupported AI intent.")))

(def ^:private schema:client
  [:map
   [:enabled? :boolean]])

(defmethod ig/init-key ::client
  [_ _cfg]
  {:enabled? (contains? cf/flags :ai-assistant)})

(defmethod ig/assert-key ::client
  [_ params]
  (assert (sm/check schema:client (merge {:enabled? true} params))))
