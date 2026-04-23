# 问： 好的
## 答： 
### Searched for 5 patterns, read 5 files (ctrl+o to expand)                                                                                              
                                                                                                                                                        
  Read 5 files (ctrl+o to expand)                                                                                                                       
                                                                                                                                                        
#### ⏺ Update(frontend/src/app/main/ui/ds/layout/admin_page.cljs)                                                                                            
  ⎿  Added 12 lines, removed 3 lines                                                                                                                    
      13  (def ^:private schema:admin-page                                                                                                              
      14    [:map                                                                                                                                       
      15     [:class {:optional true} :string]                                                                                                          
      16 +   [:size {:optional true} [:enum "default" "wide" "full"]]                                                                                   
      17 +   [:loading {:optional true} :boolean]                                                                                                       
      18     [:header {:optional true} :any]                                                                                                            
      19     [:filters {:optional true} :any]                                                                                                           
      20     [:content :any]                                                                                                                            
     ...                                                                                                                                                
      22                                                                                                                                                
      23  (mf/defc admin-page*
      24    {::mf/schema schema:admin-page}
      23 -  [{:keys [class header filters content footer] :rest props}]                                                                          
      24 -  (let [props (mf/spread-props props {:class [class (stl/css :page)]})]                                                                
      25 +  [{:keys [class size loading header filters content footer] :rest props}]                                                             
      26 +  (let [props (mf/spread-props props {:class [class (stl/css :page)                                                                    
      27 +                                             (case size                                                                                
      28 +                                               "wide" (stl/css :wide)                                                                  
      29 +                                               "full" (stl/css :full)                                                                  
      30 +                                               nil)]})]                                                                                
      31      [:> :section props
      32       (when header [:div {:class (stl/css :header)} header])
      33       (when filters [:div {:class (stl/css :filters)} filters])
      28 -     [:div {:class (stl/css :content)} content]                                                                                        
      34 +     [:div {:class (stl/css :content)}                                                                                                 
      35 +      (if loading                                                                                                                      
      36 +        [:div {:class (stl/css :loading)} "Loading..."]                                                                                
      37 +        content)]                                                                                                                      
      38       (when footer [:div {:class (stl/css :footer)} footer])]))

#### ⏺ Update(frontend/src/app/main/ui/ds/layout/page_header.cljs)
  ⎿  Added 8 lines, removed 2 lines           
      17     [:class {:optional true} :string]        
      18     [:title :string]                 
      19     [:description {:optional true} :string]
      20 +   [:breadcrumbs {:optional true} :any]                                                                                                       
      21 +   [:metadata {:optional true} :any]                                                                                                   
      22     [:actions {:optional true} :any]])                                                                                                         
      23                                                                           
      24  (mf/defc page-header*                                                                                                                         
      25    {::mf/schema schema:page-header}                                       
      24 -  [{:keys [class title description actions] :rest props}]                                                                              
      26 +  [{:keys [class title description breadcrumbs metadata actions] :rest props}]                                                         
      27    (let [props (mf/spread-props props {:class [class (stl/css :header)]})]
      28      [:> :div props
      29       [:div {:class (stl/css :content)}
      30 +      (when breadcrumbs                                                                                                                
      31 +        [:div {:class (stl/css :breadcrumbs)} breadcrumbs])                                                                            
      32        [:> heading* {:level 2} title]
      33        (when description
      30 -        [:> text* {:typography "body-small"} description])]                                                                            
      34 +        [:> text* {:typography "body-small"} description])                                                                             
      35 +      (when metadata                                                                                                                   
      36 +        [:div {:class (stl/css :metadata)} metadata])]                                                                                 
      37       (when actions
      38         [:div {:class (stl/css :actions)} actions])]))

#### ⏺ Update(frontend/src/app/main/ui/ds/layout/filter_bar.cljs)
  ⎿  Added 12 lines, removed 5 lines          
      18     [:query {:optional true} :string]        
      19     [:placeholder {:optional true} :string]
      20     [:on-query-change {:optional true} fn?]
      21 -   [:on-reset {:optional true} fn?]])                                                                                                         
      21 +   [:on-reset {:optional true} fn?]                                                                                                    
      22 +   [:actions {:optional true} :any]                                                                                                           
      23 +   [:children {:optional true} :any]])                                                                                                 
      24                                                                                                                                                
      25  (mf/defc filter-bar*                                                     
      26    {::mf/schema schema:filter-bar}
      25 -  [{:keys [class query placeholder on-query-change on-reset] :rest props}]                                                             
      27 +  [{:keys [class query placeholder on-query-change on-reset actions children] :rest props}]                                            
      28    (let [props (mf/spread-props props {:class [class (stl/css :bar)]})]
      29      [:> :div props
      30       [:div {:class (stl/css :field)}
      31        [:> input* {:placeholder (or placeholder "Search")
      32                    :value (or query "")
      33                    :on-change on-query-change}]]
      32 -     [:> button* {:variant "ghost"                                                                                                     
      33 -                  :on-click on-reset}                                                                                                  
      34 -      "Reset"]]))                                                                                                                      
      34 +     (when children                                                                                                                    
      35 +       [:div {:class (stl/css :filters)} children])                                                                                    
      36 +     [:div {:class (stl/css :actions)}                                                                                                 
      37 +      (when actions actions)                                                                                                           
      38 +      (when on-reset                                                                                                                   
      39 +        [:> button* {:variant "ghost"                                                                                                  
      40 +                     :on-click on-reset}                                                                                               
      41 +         "Reset")]]]))                                                                                                                 

  Read 1 file (ctrl+o to expand)                      
                                                       
#### ⏺ Update(frontend/src/app/main/ui/ds/layout/filter_bar.cljs)
  ⎿  Added 1 line, removed 1 line                           
      38        (when on-reset                        
      39          [:> button* {:variant "ghost"                                                                                                         
      40                       :on-click on-reset}
      41 -         "Reset")]]]))                                                                                                                        
      41 +         "Reset"])]]]))                                                                                                                       
                                                                                                                                                        
#### ⏺ Update(frontend/src/app/main/ui/ds/layout/pagination.cljs)                                                                                            
  ⎿  Added 6 lines, removed 1 line                                                                                                                      
      16     [:class {:optional true} :string]                                                                                                          
      17     [:page :int]                                                                                                                               
      18     [:total-pages :int]
      19 +   [:total {:optional true} :int]                                                                                                             
      20 +   [:page-size {:optional true} :int]                                                                                                  
      21     [:on-prev fn?]                                                                                                                             
      22     [:on-next fn?]])                                                                                                                         
      23                                                                                                                                                
      24  (mf/defc pagination*                                                     
      25    {::mf/schema schema:pagination}
      24 -  [{:keys [class page total-pages on-prev on-next] :rest props}]                                                                       
      26 +  [{:keys [class page total-pages total page-size on-prev on-next] :rest props}]                                                       
      27    (let [props (mf/spread-props props {:class [class (stl/css :pagination)]})]
      28      [:> :div props
      29 +     (when (and total page-size)                                                                                                       
      30 +       [:span {:class (stl/css :total)}                                                                                                
      31 +        (str total " items · " page-size " / page")])                                                                                  
      32       [:> button* {:variant "secondary"
      33                    :on-click on-prev
      34                    :disabled (<= page 1)}