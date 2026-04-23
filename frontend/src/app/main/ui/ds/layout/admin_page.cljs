;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.ds.layout.admin-page
  (:require-macros
   [app.main.style :as stl])
  (:require
   [rumext.v2 :as mf]))

(def ^:private schema:admin-page
  [:map
   [:class {:optional true} :string]
   [:size {:optional true} [:enum "default" "wide" "full"]]
   [:loading {:optional true} :boolean]
   [:header {:optional true} :any]
   [:filters {:optional true} :any]
   [:content :any]
   [:footer {:optional true} :any]])

(mf/defc admin-page*
  {::mf/schema schema:admin-page}
  [{:keys [class size loading header filters content footer] :rest props}]
  (let [props (mf/spread-props props {:class [class (stl/css :page)
                                             (case size
                                               "wide" (stl/css :wide)
                                               "full" (stl/css :full)
                                               nil)]})]
    [:> :section props
     (when header [:div {:class (stl/css :header)} header])
     (when filters [:div {:class (stl/css :filters)} filters])
     [:div {:class (stl/css :content)}
      (if loading
        [:div {:class (stl/css :loading)} "Loading..."]
        content)]
     (when footer [:div {:class (stl/css :footer)} footer])]))
