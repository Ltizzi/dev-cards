import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import { EndpointType } from "../utils/endpoints";

const URL = "https://dev-cards.sliplane.app/"; //http://localhost:8080";

const apiCaller = axios.create({
  baseURL: URL,
  headers: {
    "Content-Type": "application/json",
  },
});

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = token;
      config.headers["Content-Type"] = "application/json";
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export function useApiCall<T>() {
  const get = async (
    endpoint: EndpointType,
    config?: AxiosRequestConfig
  ): Promise<T> => {
    try {
      const url = getUrl(endpoint);
      const response: AxiosResponse<T> = await axios.get(url, config);
      return response.data;
    } catch (err: any) {
      return err.message;
    }
  };

  const post = async (
    endpoint: EndpointType,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<T> => {
    try {
      const response: AxiosResponse<T> = await axios.post(
        getUrl(endpoint),
        data,
        config
      );
      return response.data;
    } catch (err: any) {
      return err.message;
    }
  };

  const patch = async (
    endpoint: EndpointType,
    data: any,
    config?: AxiosRequestConfig
  ): Promise<T> => {
    try {
      const response: AxiosResponse<T> = await axios.patch(
        getUrl(endpoint),
        data,
        config
      );
      return response.data;
    } catch (err: any) {
      return err.message;
    }
  };

  const del = async (
    endpoint: EndpointType,
    config?: AxiosRequestConfig
  ): Promise<T> => {
    try {
      const response: AxiosResponse<T> = await axios.delete(
        getUrl(endpoint),
        config
      );
      return response.data;
    } catch (err: any) {
      return err.message;
    }
  };

  return {
    get,
    post,
    patch,
    del,
  };
}

const getUrl = (endpoint: EndpointType): string => {
  return `${URL}${endpoint}`;
};
