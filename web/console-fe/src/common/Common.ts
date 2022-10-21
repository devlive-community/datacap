const token = 'AuthToken';
const getCurrentUserId = () => {
  return JSON.parse(localStorage.getItem(token) || '{}').id;
}

export default {
  token: token,
  getCurrentUserId: getCurrentUserId
}
