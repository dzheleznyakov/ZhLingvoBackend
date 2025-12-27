import axios, { type AxiosRequestConfig } from 'axios';

const basicHeaders = () => ({
  'Access-Control-Allow-Origin': '*',
  Cookies: document.cookie,
});

const prepareConfig = (customConfig: AxiosRequestConfig = {}) => {
  const { headers: customHeaders } = customConfig;
  const headers = { ...basicHeaders(), ...customHeaders };
  return { ...customConfig, headers };
};

const baseURL = 'http://localhost:8080';
// const baseURL = 'http://MacBook-Pro-2.local:8080';

const instance = axios.create({
  baseURL: `${baseURL}/api`,
});

const api = {
  ...instance,
  get: (url: string, config?: AxiosRequestConfig) => instance.get(url, prepareConfig(config)),
  post: (url: string, data: unknown, config?: AxiosRequestConfig) => instance.post(url, data, prepareConfig(config)),
  put: (url: string, data: unknown, config?: AxiosRequestConfig) => instance.put(url, data, prepareConfig(config)),
  delete: (url: string, config?: AxiosRequestConfig) => instance.delete(url, prepareConfig(config)),
  login: (url: string, data: unknown, config?: AxiosRequestConfig) =>
    instance.post(baseURL + url, data, prepareConfig(config)),
};

export default api;
