import {ResponseModel} from "@/model/ResponseModel";
import axios from 'axios';
import Common from "@/common/Common";
import {Message} from "view-ui-plus";
import router from "@/router";

axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

export class HttpCommon
{
  private configure;

  constructor()
  {
    if (process.env.NODE_ENV === 'development' ||
      window.location.hostname === 'localhost') {
      axios.defaults.baseURL = 'http://139.198.158.103:9096';
    }
    else {
      axios.defaults.baseURL = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
    }

    const auth = JSON.parse(localStorage.getItem(Common.token) || '{}');
    this.configure = {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': auth.type + ' ' + auth.token
      },
      cancelToken: undefined,
      params: undefined
    }
  }

  handlerSuccessful(result: any): ResponseModel
  {
    const data = result.data;
    let messageData = data.message;
    if (data.message instanceof Array) {
      const messages = new Array();
      data.message.forEach((element: { field: string; message: string; }) => {
        messages.push(element.field + ': ' + element.message);
      });
      messageData = messages.join('\r\n');
    }
    const response: ResponseModel = {
      code: data.code,
      data: data.data,
      message: messageData,
      status: data.status
    };

    // If the authorization key does not match, clear the local token reload page
    if (response.code === 4003) {
      Message.error(response.message);
      router.push('/common/not_authorized');
    }
    if (response.code === 5000) {
      Message.error(response.message);
    }
    return response;
  }

  handlerFailed(error: any): ResponseModel
  {
    const response: ResponseModel = {
      code: 0,
      message: error.message,
      status: false
    };
    if (error.code === 'ERR_NETWORK') {
      router.push("/common/not_network");
    }
    return response;
  }

  get(url: string, params?: any): Promise<ResponseModel>
  {
    return new Promise((resolve) => {
      this.configure.params = params;
      axios.get(url, this.configure)
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          Message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }

  post(url: string, data = {}, cancelToken?: any): Promise<ResponseModel>
  {
    return new Promise((resolve) => {
      this.configure.cancelToken = cancelToken;
      axios.post(url, data, this.configure)
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          Message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }

  put(url: string, data = {}): Promise<ResponseModel>
  {
    return new Promise((resolve) => {
      axios.put(url, data, this.configure)
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          Message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }

  delete(url: string): Promise<ResponseModel>
  {
    return new Promise((resolve) => {
      axios.delete(url, this.configure)
        .then(result => {
          resolve(this.handlerSuccessful(result));
        }, error => {
          Message.error(error.message);
          resolve(this.handlerFailed(error));
        });
    });
  }

  getAxios()
  {
    return axios;
  }
}
