import { APIRequestContext } from "@playwright/test";

export interface ApiResponse<T> {
  status: number;
  data: T;
}

export class ApiClient {
  private request: APIRequestContext;

  constructor(request: APIRequestContext) {
    this.request = request;
  }

  async get<T>(endpoint: string): Promise<ApiResponse<T>> {
    const response = await this.request.get(endpoint);
    const data = await response.json();
    return { status: response.status(), data: data as T };
  }

  async post<T>(endpoint: string, body: unknown): Promise<ApiResponse<T>> {
    const response = await this.request.post(endpoint, { data: body });
    const data = await response.json();
    return { status: response.status(), data: data as T };
  }

  async put<T>(endpoint: string, body: unknown): Promise<ApiResponse<T>> {
    const response = await this.request.put(endpoint, { data: body });
    const data = await response.json();
    return { status: response.status(), data: data as T };
  }

  async delete<T>(endpoint: string): Promise<ApiResponse<T>> {
    const response = await this.request.delete(endpoint);
    const data = await response.json().catch(() => null);
    return { status: response.status(), data: data as T };
  }

  async patch<T>(endpoint: string, body: unknown): Promise<ApiResponse<T>> {
    const response = await this.request.patch(endpoint, { data: body });
    const data = await response.json();
    return { status: response.status(), data: data as T };
  }
}
