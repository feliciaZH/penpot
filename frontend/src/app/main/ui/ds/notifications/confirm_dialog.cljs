;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.ds.notifications.confirm-dialog
  (:require-macros
   [app.main.style :as stl])
  (:require
   [app.main.ui.ds.buttons.button :refer [button*]]
   [rumext.v2 :as mf]))

(def ^:private schema:confirm-dialog
  [:map
   [:class {:optional true} :string]
   [:title :string]
   [:message :string]
   [:accept-label :string]
   [:cancel-label :string]
   [:on-accept fn?]
   [:on-cancel fn?]
   [:accept-variant {:optional true} [:enum "primary" "destructive"]]])

(mf/defc confirm-dialog*
  {::mf/schema schema:confirm-dialog}
  [{:keys [class title message accept-label cancel-label on-accept on-cancel accept-variant]
    :rest props}]
  (let [props (mf/spread-props props {:class [class (stl/css :overlay)]})]
    [:> :div props
     [:div {:class (stl/css :dialog)}
      [:h3 {:class (stl/css :title)} title]
      [:p {:class (stl/css :message)} message]
      [:div {:class (stl/css :actions)}
       [:> button* {:variant "secondary" :on-click on-cancel} cancel-label]
       [:> button* {:variant (or accept-variant "primary") :on-click on-accept} accept-label]]]]))
