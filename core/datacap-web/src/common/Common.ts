import {EditorConfigure} from "@/model/User";

const token = 'DataCapAuthToken';
const menu = 'DataCapAvailableMenus';
const userEditorConfigure = 'DataCapUserEditorConfigure';
const getCurrentUserId = () => {
  return JSON.parse(localStorage.getItem(token) || '{}').id;
}

const getEditorConfigure = (): EditorConfigure => {
  const localEditorConfigure = localStorage.getItem(userEditorConfigure);
  return localEditorConfigure ? JSON.parse(localEditorConfigure) : new EditorConfigure();
}

export default {
  token: token,
  menu: menu,
  getCurrentUserId: getCurrentUserId,
  userEditorConfigure: userEditorConfigure,
  getEditorConfigure: getEditorConfigure()
}
