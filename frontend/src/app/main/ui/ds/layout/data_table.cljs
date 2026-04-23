;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.ds.layout.data-table
  (:require-macros
   [app.main.style :as stl])
  (:require
   [rumext.v2 :as mf]))

(def ^:private schema:data-table
  [:map
   [:class {:optional true} :string]
   [:columns [:vector [:map
                       [:id :string]
                       [:label :string]
                       [:class {:optional true} :string]
                       [:align {:optional true} [:enum "start" "center" "end"]]]]]
   [:rows [:vector [:map
                    [:id :string]
                    [:cells [:vector :any]]]]]
   [:empty-text {:optional true} :string]
   [:loading {:optional true} :boolean]
   [:caption {:optional true} :string]
   [:on-row-click {:optional true} fn?]])

(mf/defc data-table*
  {::mf/schema schema:data-table}
  [{:keys [class columns rows empty-text loading caption on-row-click] :rest props}]
  (let [props (mf/spread-props props {:class [class (stl/css :table)]})]
    [:> :table props
     (when caption
       [:caption {:class (stl/css :caption)} caption])
     [:thead {:class (stl/css :header)}
      [:tr
       (for [{:keys [id label class align]} columns]
         [:th {:key id
               :scope "col"
               :class [class (stl/css :header-cell)
                       (case align
                         "center" (stl/css :align-center)
                         "end" (stl/css :align-end)
                         nil)]}
          label])]]
     [:tbody {:class (stl/css :body)}
      (cond
        loading
        [:tr
         [:td {:class (stl/css :state)
               :col-span (count columns)}
          "Loading..."]]

        (empty? rows)
        [:tr
         [:td {:class (stl/css :state)
               :col-span (count columns)}
          (or empty-text "No data available")]]

        :else
        (for [{:keys [id cells] :as row} rows]
          [:tr {:key id
                :class (stl/css-case :row true
                                     :clickable (some? on-row-click))
                :on-click (when on-row-click #(on-row-click row))}
           (for [[idx cell] (map-indexed vector cells)
                 :let [column (get columns idx)
                       align (:align column)]]
             [:td {:key (str id "-" idx)
                   :class [(stl/css :cell)
                           (case align
                             "center" (stl/css :align-center)
                             "end" (stl/css :align-end)
                             nil)]}
              cell])]))]]))
