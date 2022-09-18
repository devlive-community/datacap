import axios from 'axios';
import {ResponseModel} from "@/model/ResponseModel";
import {message} from "ant-design-vue";

axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

const configure = {
  headers: {
    'Content-Type': 'application/json'
  }
}

export class HttpCommon
{
  constructor()
  {
    if (process.env.NODE_ENV === 'development' ||
      window.location.hostname === 'localhost') {
      axios.defaults.baseURL = 'http://localhost:9096'
    }
  }

  get(url: string): Promise<ResponseModel>
  {
    return new Promise((resolve) => {
      axios.get(url)
        .then(result => {
          const data = result.data;
          const response: ResponseModel = {
            code: data.code,
            data: data.data,
            message: data.message,
            status: true
          };
          resolve(response);
        }, error => {
          message.error(error.message);
          const response: ResponseModel = {
            code: 0,
            message: error.message,
            status: false
          };
          resolve(response);
        });
    });
  }

  post(url: string, data = {}): Promise<ResponseModel>
  {
    return new Promise((resolve) => {
      axios.post(url, data, configure)
        .then(result => {
          const data = result.data;
          const response: ResponseModel = {
            code: data.code,
            data: data.data,
            message: data.message,
            status: true
          };
          resolve(response);
        }, error => {
          message.error(error.message);
          const response: ResponseModel = {
            code: 0,
            message: error.message,
            status: false
          };
          resolve(response);
        });
    });
  }

  delete(url: string): Promise<ResponseModel>
  {
    return new Promise((resolve) => {
      axios.delete(url)
        .then(result => {
          const data = result.data;
          const response: ResponseModel = {
            code: data.code,
            data: data.data,
            message: data.message,
            status: true
          };
          resolve(response);
        }, error => {
          message.error(error.message);
          const response: ResponseModel = {
            code: 0,
            message: error.message,
            status: false
          };
          resolve(response);
        });
    });
  }
}
