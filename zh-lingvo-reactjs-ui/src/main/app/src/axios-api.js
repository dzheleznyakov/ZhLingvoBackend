import axios from 'axios';

const basicHeaders = () => ({
  'Access-Control-Allow-Origin': '*',
  'Cookies': document.cookie,
});

const prepareConfig = (customConfig = {}) => {
  const { headers: customHeaders } = customConfig;
  const headers = { ...basicHeaders(), ...customHeaders };
  return { ...customConfig, headers };
};

// export const baseURL = 'http://192.168.1.116:8080';
export const baseURL = 'http://10.101.11.117:8080';

const instance = axios.create({
    baseURL: `${baseURL}/api`,
});
  
export default {
  ...instance,
  get: (url, config) => instance.get(url, prepareConfig(config) ),
  post: (url, data, config) => instance.post(url, data, prepareConfig(config)),
  put: (url, data, config) => instance.put(url, data, prepareConfig(config)),
  delete: (url, config) => instance.delete(url, prepareConfig(config) ),
  login: (url, data, config) => instance.post(baseURL + url, data, prepareConfig(config)),
};
  