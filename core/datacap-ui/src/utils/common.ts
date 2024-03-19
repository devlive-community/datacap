const token = 'DataCapAuthToken'
const menu = 'DataCapAvailableMenus'
const userEditorConfigure = 'DataCapUserEditorConfigure'

/**
 * Retrieves the current user's ID from local storage.
 *
 * @return {number} The current user's ID.
 */
const getCurrentUserId = (): number => {
    return JSON.parse(localStorage.getItem(token) || '{}').id
}

export default {
    token: token,
    menu: menu,
    getCurrentUserId: getCurrentUserId,
    userEditorConfigure: userEditorConfigure
}
