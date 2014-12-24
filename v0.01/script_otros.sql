## select con filtros de rol y user

SELECT * FROM w_step_work w 
left join w_process_work wpw on w.id_work=wpw.id 
left join w_step_def wsd on w.id_current_step=wsd.id 
left join w_step_role wsr on wsd.id=wsr.id_step 
left join w_step_user wsu on wsd.id=wsu.id_step AND wsu.id_user = 1001 
left join w_step_work_assignment wswa on w.id=wswa.id_step_work  
WHERE  ( w.decided_date IS NULL  )  
				AND  
		( ( wsr.id_role in (select wur.id_role from w_user_role wur where wur.id_user=1001 )) 
				OR   ( wsu.id_user=1001 ) 
				OR  ( wswa.id_role in (select wur.id_role from w_user_role wur where wur.id_user=1001 )) 
				OR   ( wswa.id_user =1001 ) )  
GROUP BY w.id  ORDER BY w.deadline_date, w.deadline_time, w.arriving_date  DESC;

##