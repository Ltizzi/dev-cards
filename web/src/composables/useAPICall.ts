import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import { EndpointType } from "../utils/endpoints";

const URL = "http://localhost:8080";

export function useApiCall<T>() {
  const get = async (
    endpoint: EndpointType,
    config?: AxiosRequestConfig
  ): Promise<T> => {
    try {
      const response: AxiosResponse<T> = await axios.get(
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
      const response: AxiosResponse<T> = await axios.post(
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
      const response: AxiosResponse<T> = await axios.patch(
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
