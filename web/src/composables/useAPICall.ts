import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import { EndpointType } from "../utils/endpoints";

const URL = "http://localhost:8080";

const apiCaller = axios.create({
  baseURL: URL,
  headers: {
    "Content-Type": "application/json",
  },
});

apiCaller.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = token;
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
      const response: AxiosResponse<T> = await apiCaller.get(
        getUrl(endpoint),
        config
      );
      return response.data;
    } catch (err: any) {
      return err.message;
    }
  };

  const post = async (
    endpoint: EndpointType,
    config?: AxiosRequestConfig
  ): Promise<T> => {
    try {
      const response: AxiosResponse<T> = await apiCaller.post(
        getUrl(endpoint),
        config
      );
      return response.data;
    } catch (err: any) {
      return err.message;
    }
  };

  const patch = async (
    endpoint: EndpointType,
    config?: AxiosRequestConfig
  ): Promise<T> => {
    try {
      const response: AxiosResponse<T> = await apiCaller.patch(
        getUrl(endpoint),
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
      const response: AxiosResponse<T> = await apiCaller.delete(
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
