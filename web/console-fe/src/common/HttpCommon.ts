import { ResponseModel } from "@/model/ResponseModel";
import { message } from "ant-design-vue";
import axios from 'axios';

axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

const configure = {
  headers: {
    'Content-Type': 'application/json'
  }
}

export class HttpCommon {
  constructor() {
    if (process.env.NODE_ENV === 'development' ||
      window.location.hostname === 'localhost') {
      axios.defaults.baseURL = 'http://localhost:9096';
    } else {
      axios.defaults.baseURL = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
    }
  }

  handlerSuccessful(result: any): ResponseModel {
    const data = result.data;
    let message = data.message;
    if (data.message instanceof Array) {
      const messages = new Array();
      data.message.forEach((element: { field: string; message: string; }) => {
        messages.push(element.field + ': ' + element.message);
      });
      message = messages.join('\r\n');
    }
    const response: ResponseModel = {
      code: data.code,
      data: data.data,
      message: message,
      status: data.status
    };
    return response;
  }

  handlerFailed(error: any): ResponseModel {
    const response: ResponseModel = {
      code: 0,
      message: error.message,
      status: false
    };
    return response;
  }

  get(url: string, params?: any): Promise<ResponseModel> {
    return new Promise((resolve) => {
      axios.get(url, { params: params })
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }

  post(url: string, data = {}): Promise<ResponseModel> {
    return new Promise((resolve) => {
      axios.post(url, data, configure)
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }

  put(url: string, data = {}): Promise<ResponseModel> {
    return new Promise((resolve) => {
      axios.put(url, data, configure)
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }

  delete(url: string): Promise<ResponseModel> {
    return new Promise((resolve) => {
      axios.delete(url)
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }
}
