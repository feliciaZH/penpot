;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.ds.layout.pagination
  (:require-macros
   [app.main.style :as stl])
  (:require
   [app.main.ui.ds.buttons.button :refer [button*]]
   [rumext.v2 :as mf]))

(def ^:private schema:pagination
  [:map
   [:class {:optional true} :string]
   [:page :int]
   [:total-pages :int]
   [:total {:optional true} :int]
   [:page-size {:optional true} :int]
   [:on-prev fn?]
   [:on-next fn?]])

(mf/defc pagination*
  {::mf/schema schema:pagination}
  [{:keys [class page total-pages total page-size on-prev on-next] :rest props}]
  (let [props (mf/spread-props props {:class [class (stl/css :pagination)]})]
    [:> :div props
     (when (and total page-size)
       [:span {:class (stl/css :total)}
        (str total " items · " page-size " / page")])
     [:> button* {:variant "secondary"
                  :on-click on-prev
                  :disabled (<= page 1)}
      "Prev"]
     [:span {:class (stl/css :page-info)}
      (str page " / " total-pages)]
     [:> button* {:variant "secondary"
                  :on-click on-next
                  :disabled (>= page total-pages)}
      "Next"]]))
