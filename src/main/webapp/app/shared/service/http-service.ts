import axios from 'axios';

const TIMEOUT = 1 * 80 * 1000;
axios.defaults.timeout = TIMEOUT;
axios.defaults.headers = { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' };
if (process.env.NODE_ENV === 'development') {
  axios.defaults.baseURL = 'http://localhost:8181/desy/';
}
const onRequestSuccess = config => config;
const onResponseSuccess = response => response;

axios.interceptors.request.use(onRequestSuccess);
axios.interceptors.response.use(onResponseSuccess);

export default {
  get: axios.get,
  post: axios.post,
  put: axios.put,
  delete: axios.delete
};
