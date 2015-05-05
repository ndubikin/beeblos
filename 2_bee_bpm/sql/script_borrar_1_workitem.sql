-- Borrar un workitem x
set @idWorkItem=100; -- el ID lo obtenemos de la tabla w_process_work (tenemos 1 registro x c/ woritem)
delete from wmt_1 where process_work_id=@idWorkItem; -- borramos tabla con info de datafields
delete from w_step_work_sequence where step_work_id in (Select id from w_step_work where id_work=@idWorkItem) AND id>1; -- borramos la traza
delete from w_step_work_assignment where id_step_work in (Select id from w_step_work where id_work=@idWorkItem) AND id>1; -- borramos las asignaciones en tiempo de ejecución
delete from w_step_work where id_work=@idWorkItem; -- borramos ciclo vida

delete from w_user_role_work where id_process_work=@idWorkItem and id>0;  -- borramos la cabecera de la instancia

delete from w_process_work where id=@idWorkItem;  -- borramos la cabecera de la instancia