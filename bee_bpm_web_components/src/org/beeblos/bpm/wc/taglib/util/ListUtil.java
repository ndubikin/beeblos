package org.beeblos.bpm.wc.taglib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WUserDef;

public class ListUtil extends CoreManagedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListUtil() {

	}

	public static void updateStepUserRelatedList(String strUserList,
			WStepDef currentWStepDef, Integer userId)
			throws NumberFormatException, WUserDefException {

		WUserDef wUserDef;
		WUserDefBL wUserDefBL = new WUserDefBL();

		boolean isInList = false;

		if (currentWStepDef.getUsersRelated() != null) {

			for (String s : strUserList.split(",")) {

				isInList = false;

				for (WStepUser wsu : currentWStepDef.getUsersRelated()) {

					if (wsu.getUser() != null
							&& wsu.getUser().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}
				}

				if (!isInList) {

					wUserDef = wUserDefBL.getWUserDefByPK(Integer.parseInt(s),
							null);
					currentWStepDef
							.addUser(wUserDef, false, null, null, userId);

				}

			}

			ArrayList<WStepUser> removeList = new ArrayList<WStepUser>();

			for (WStepUser wsu : currentWStepDef.getUsersRelated()) {

				isInList = false;

				for (String s : strUserList.split(",")) {

					if (wsu.getUser() != null
							&& wsu.getUser().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}

				}

				if (!isInList) {

					removeList.add(wsu);

				}

			}

			for (WStepUser wsu : removeList) {

				currentWStepDef.getUsersRelated().remove(wsu);

			}

		}

	}

	public static void updateStepRoleRelatedList(String strRoleList,
			WStepDef currentWStepDef, Integer userId)
			throws NumberFormatException, WRoleDefException {

		WRoleDef wRoleDef;
		WRoleDefBL wRoleDefBL = new WRoleDefBL();

		boolean isInList = false;

		if (currentWStepDef.getRolesRelated() != null) {

			for (String s : strRoleList.split(",")) {

				isInList = false;

				for (WStepRole wsu : currentWStepDef.getRolesRelated()) {

					if (wsu.getRole() != null
							&& wsu.getRole().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}
				}

				if (!isInList) {

					wRoleDef = wRoleDefBL.getWRoleDefByPK(Integer.parseInt(s),
							null);
					currentWStepDef
							.addRole(wRoleDef, false, null, null, userId);

				}

			}

			ArrayList<WStepRole> removeList = new ArrayList<WStepRole>();

			for (WStepRole wsu : currentWStepDef.getRolesRelated()) {

				isInList = false;

				for (String s : strRoleList.split(",")) {

					if (wsu.getRole() != null
							&& wsu.getRole().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}

				}

				if (!isInList) {

					removeList.add(wsu);

				}

			}

			for (WStepRole wsu : removeList) {

				currentWStepDef.getRolesRelated().remove(wsu);

			}

		}

	}

	public static void updateProcessUserRelatedList(String strUserList,
			WProcessDef currentWProcessDef, Integer userId)
			throws NumberFormatException, WUserDefException {

		WUserDef wUserDef;
		WUserDefBL wUserDefBL = new WUserDefBL();

		boolean isInList = false;

		if (currentWProcessDef.getUsersRelated() != null) {

			for (String s : strUserList.split(",")) {

				isInList = false;

				for (WProcessUser wsu : currentWProcessDef.getUsersRelated()) {

					if (wsu.getUser() != null
							&& wsu.getUser().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}
				}

				if (!isInList) {

					wUserDef = wUserDefBL.getWUserDefByPK(Integer.parseInt(s),
							null);
					currentWProcessDef.addUser(wUserDef, false, null, null,
							userId);

				}

			}

			ArrayList<WProcessUser> removeList = new ArrayList<WProcessUser>();

			for (WProcessUser wsu : currentWProcessDef.getUsersRelated()) {

				isInList = false;

				for (String s : strUserList.split(",")) {

					if (wsu.getUser() != null
							&& wsu.getUser().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}

				}

				if (!isInList) {

					removeList.add(wsu);

				}

			}

			for (WProcessUser wsu : removeList) {

				currentWProcessDef.getUsersRelated().remove(wsu);

			}

		}

	}

	public static void updateProcessRoleRelatedList(String strRoleList,
			WProcessDef currentWProcessDef, Integer userId)
			throws NumberFormatException, WRoleDefException {

		WRoleDef wRoleDef;
		WRoleDefBL wRoleDefBL = new WRoleDefBL();

		boolean isInList = false;

		if (currentWProcessDef.getRolesRelated() != null) {

			for (String s : strRoleList.split(",")) {

				isInList = false;

				for (WProcessRole wsu : currentWProcessDef.getRolesRelated()) {

					if (wsu.getRole() != null
							&& wsu.getRole().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}
				}

				if (!isInList) {

					wRoleDef = wRoleDefBL.getWRoleDefByPK(Integer.parseInt(s),
							null);
					currentWProcessDef.addRole(wRoleDef, false, null, null,
							userId);

				}

			}

			ArrayList<WProcessRole> removeList = new ArrayList<WProcessRole>();

			for (WProcessRole wsu : currentWProcessDef.getRolesRelated()) {

				isInList = false;

				for (String s : strRoleList.split(",")) {

					if (wsu.getRole() != null
							&& wsu.getRole().getId()
									.equals(Integer.parseInt(s))) {

						isInList = true;
						break;

					}

				}

				if (!isInList) {

					removeList.add(wsu);

				}

			}

			for (WProcessRole wsu : removeList) {

				currentWProcessDef.getRolesRelated().remove(wsu);

			}

		}

	}

}
