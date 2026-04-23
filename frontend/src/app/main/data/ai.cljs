;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.data.ai
  (:require
   [app.main.data.notifications :as ntf]
   [app.main.repo :as rp]
   [beicon.v2.core :as rx]
   [clojure.string :as str]
   [potok.v2.core :as ptk]))

(defn generate-team-announcement
  [text]
  (ptk/reify ::generate-team-announcement
    ptk/WatchEvent
    (watch [_ _ _]
      (->> (rp/cmd! :ai-generate-team-announcement {:context {:scope :team :text text}})
           (rx/map (fn [{:keys [content]}]
                     (ntf/success content)))))))

(defn suggest-resource-tags
  [text]
  (ptk/reify ::suggest-resource-tags
    ptk/WatchEvent
    (watch [_ _ _]
      (->> (rp/cmd! :ai-suggest-resource-tags {:context {:scope :resource :text text}})
           (rx/map (fn [{:keys [tags]}]
                     (ntf/info (str "AI tags: " (str/join ", " tags)))))))))

(defn summarize-ops-events
  [text]
  (ptk/reify ::summarize-ops-events
    ptk/WatchEvent
    (watch [_ _ _]
      (->> (rp/cmd! :ai-summarize-ops-events {:context {:scope :ops :text text}})
           (rx/map (fn [{:keys [summary]}]
                     (ntf/success summary)))))))
