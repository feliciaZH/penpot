;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.ds.layout.filter-bar
  (:require-macros
   [app.main.style :as stl])
  (:require
   [app.main.ui.ds.buttons.button :refer [button*]]
   [app.main.ui.ds.controls.input :refer [input*]]
   [rumext.v2 :as mf]))

(def ^:private schema:filter-bar
  [:map
   [:class {:optional true} :string]
   [:query {:optional true} :string]
   [:placeholder {:optional true} :string]
   [:on-query-change {:optional true} fn?]
   [:on-reset {:optional true} fn?]
   [:actions {:optional true} :any]
   [:children {:optional true} :any]])

(mf/defc filter-bar*
  {::mf/schema schema:filter-bar}
  [{:keys [class query placeholder on-query-change on-reset actions children] :rest props}]
  (let [props (mf/spread-props props {:class [class (stl/css :bar)]})]
    [:> :div props
     [:div {:class (stl/css :field)}
      [:> input* {:placeholder (or placeholder "Search")
                  :value (or query "")
                  :on-change on-query-change}]]
     (when children
       [:div {:class (stl/css :filters)} children])
     [:div {:class (stl/css :actions)}
      (when actions actions)
      (when on-reset
        [:> button* {:variant "ghost"
                     :on-click on-reset}
         "Reset"])]]]))
