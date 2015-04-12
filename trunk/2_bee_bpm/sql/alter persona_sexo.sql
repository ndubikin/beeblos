update persona set persona_sexo = '0' where persona_sexo LIKE 'VARON';
update persona set persona_sexo = '0' where persona_sexo LIKE 'Masculino';
update persona set persona_sexo = '1' where persona_sexo LIKE 'MUJER';
update persona set persona_sexo = '1' where persona_sexo LIKE 'Femenino';
ALTER TABLE persona 
CHANGE COLUMN persona_sexo persona_sexo BIT(1) DEFAULT b'0'  ;
select * from persona;