package org.beeblos.bpm.core.model.util;

	public enum RouteEvaluationOrder {

	    FIRST_TRUE_CONDITION('F', "First true condition"),
	    ALL_TRUE_CONDITION('A', "All true condition");    
	    
	    private Character id;
	    private String name;
	    
	    private RouteEvaluationOrder(Character id, String nombre) {
	        this.id	 = id;
	        this.name = nombre;
	    }

		public Character getId() {
			return id;
		}

		public void setId(Character id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	};


