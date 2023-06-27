const token = 'DataCapAuthToken';
const menu = 'DataCapAvailableMenus';
const getCurrentUserId = () => {
  return JSON.parse(localStorage.getItem(token) || '{}').id;
}

export default {
  token: token,
  menu: menu,
  getCurrentUserId: getCurrentUserId
}
