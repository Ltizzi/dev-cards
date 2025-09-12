import { defineStore } from "pinia";
import { useApiCall } from "../composables/useAPICall";
import {
  APIResponse,
  CalendarItem,
  UserCalendar,
  WorkspaceCalendar,
} from "../utils/types";
import { useUIStore } from "./ui.store";
import { useUserStore } from "./user.store";
import { EndpointType } from "../utils/endpoints";
import { utils } from "../utils/utils";

const apiCall = useApiCall();

export const useCalendarStore = defineStore("calendars", {
  state: () => ({
    userCalendar: {} as UserCalendar,
    workspaceCalendar: {} as WorkspaceCalendar,
    offlineMode: false,
  }),
  actions: {
    checkOfflineMode() {
      const UIStore = useUIStore();
      this.offlineMode = UIStore.checkOfflineMode();
      return this.offlineMode;
    },
    setUserCalendar(cal: UserCalendar) {
      this.userCalendar = cal;
    },
    setWorkspaceCalendar(cal: WorkspaceCalendar) {
      this.workspaceCalendar = cal;
    },
    async getUserCalendar() {
      this.checkOfflineMode();
      const userStore = useUserStore();
      const user = userStore.getCurrent();
      if (this.offlineMode) {
        if (!this.userCalendar.calendar_id) {
          const calendar = await this.getLocalStorageUserCalendarByUserId(
            user.user_id
          );
          if (calendar && calendar.owner.user_id === user.user_id) {
            this.setUserCalendar(calendar);
            return this.userCalendar;
          }
        } else return this.userCalendar;
      } else return await this.getUserCalendarByUserId(user.user_id);
    },
    async getUserCalendarByUserId(id: number) {
      if (
        this.userCalendar &&
        this.userCalendar.owner &&
        this.userCalendar.owner.user_id === id
      )
        return this.userCalendar;
      else {
        const response = (await apiCall.get(
          EndpointType.CALENDAR_USER_BY_USER_ID,
          {
            params: {
              user_id: id,
            },
          }
        )) as UserCalendar;
        if (response && response.owner.user_id === id) {
          this.setUserCalendar(response);
          return this.userCalendar;
        }
      }
    },
    async getUserCalendarById(id: string) {
      const response = (await apiCall.get(EndpointType.CALENDAR_USER_BY_ID, {
        params: { id: id },
      })) as UserCalendar;
      if (response && response.calendar_id === id) {
        this.setUserCalendar(response);
        return this.userCalendar;
      }
    },
    async getLocalStorageUserCalendarByUserId(id: number) {
      const user_calendars = await this.getLocalStorageUsersCalendars();
      if (user_calendars && user_calendars.length > 0) {
        return user_calendars.filter(
          (uc: UserCalendar) => uc.owner.user_id === id
        )[0];
      } else return null;
    },
    async getLocalStorageUserCalendarByCalendarId(id: string) {
      const user_calendars = await this.getLocalStorageUsersCalendars();
      if (user_calendars && user_calendars.length > 0) {
        return user_calendars.filter(
          (uc: UserCalendar) => uc.calendar_id === id
        )[0];
      } else null;
    },
    async getLocalStorageUsersCalendars() {
      return JSON.parse(
        localStorage.getItem("local_users_calendars") as string
      ) as UserCalendar[];
    },
    async addItemToUserCalendar(calendar_id: string, item: CalendarItem) {
      this.checkOfflineMode();
      const userStore = useUserStore();
      const user = userStore.getCurrent();
      if (this.offlineMode) {
        const calendar =
          this.getLocalStorageUserCalendarByCalendarId(calendar_id);
        item.id = utils.generateUUID();
        item.created_at = new Date();
        item.updated_at = new Date();
        //TODO: local save flow
      } else {
        const response = (await apiCall.post(
          EndpointType.CALENDAR_USER_ADD_ITEM,
          item,
          {
            params: {
              calendar_id: calendar_id,
              user_id: user.user_id,
            },
          }
        )) as UserCalendar;
        if (response.calendar_id === this.userCalendar.calendar_id) {
          this.setUserCalendar(response);
          return this.userCalendar;
        }
      }
    },
    async updateUserCalendarItem(calendar_id: string, item: CalendarItem) {
      this.checkOfflineMode();
      const userStore = useUserStore();
      const user = userStore.getCurrent();
      if (this.offlineMode) {
        //TODO: local update user calendar item workflow
      } else {
        const response = (await apiCall.patch(
          EndpointType.CALENDAR_USER_UPDATE_ITEM,
          item,
          {
            params: {
              calendar_id: calendar_id,
              user_id: user.user_id,
            },
          }
        )) as CalendarItem;
        if (response.id === item.id) {
          this.userCalendar = (await this.getUserCalendarById(
            calendar_id
          )) as UserCalendar;
          return response;
        }
      }
    },
    async removeItemFromUserCalendar(
      calendar_id: string,
      calendarItemId: string
    ) {
      this.checkOfflineMode();
      const userStore = useUserStore();
      const user = userStore.getCurrent();
      if (this.offlineMode) {
        //TODO: local remove item from user calendar workflow
      } else {
        const response = (await apiCall.del(
          EndpointType.CALENDAR_USER_REMOVE_ITEM,
          { params: { calendar_id: calendarItemId, user_id: user.user_id } }
        )) as APIResponse;
        let res = response.message.split("-")[0];
        if (res.toLowerCase() === "ok") {
          const updatedCalendar = (await this.getUserCalendarById(
            calendar_id
          )) as UserCalendar;
          this.setUserCalendar(updatedCalendar);
          return this.userCalendar;
        }
      }
    },
  },
});
