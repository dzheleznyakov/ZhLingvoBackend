import axios from 'axios';

export const headers = {
  'Access-Control-Allow-Origin': '*',
};

export const baseURL = 'http://192.168.1.116:8080';

const instance = axios.create({
    baseURL: `${baseURL}/api`,
    headers,
})
  
export default instance;
  