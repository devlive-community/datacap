const token = 'DataCapAuthToken';
const menu = 'DataCapAvailableMenus';
const userEditorConfigure = 'DataCapUserEditorConfigure';
const getCurrentUserId = () => {
    return JSON.parse(localStorage.getItem(token) || '{}').id;
}

export default {
    token: token,
    menu: menu,
    getCurrentUserId: getCurrentUserId,
    userEditorConfigure: userEditorConfigure
}
