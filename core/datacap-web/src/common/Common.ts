const token = 'AuthToken';
const menu = 'AvailableMenus';
const getCurrentUserId = () => {
  return JSON.parse(localStorage.getItem(token) || '{}').id;
}

export default {
  token: token,
  menu: menu,
  getCurrentUserId: getCurrentUserId
}
