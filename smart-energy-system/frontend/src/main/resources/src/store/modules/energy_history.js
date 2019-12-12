import Axios from 'axios';
import {
  ADD_ENERGY_USAGE,
  ADD_ENERGY_PRODUCTION,
  ADD_BALANCE,
  SET_ENERGY_USAGES,
  SET_ENERGY_PRODUCTIONS,
  SET_BALANCES,
  SET_ENERGY_STORAGE,
  ENERGY_USAGES,
  ENERGY_PRODUCTIONS,
  BALANCES,
  ENERGY_STORAGE,
  DATAPOINTS_RECEIVED,
  SET_CONNECTION,
  MAKE_CONNECTION,
  SUBSCRIBE,
  UNSUBSCRIBE,
  GET_ENERGY_USAGE_HISTORY,
  GET_ENERGY_PRODUCTION_HISTORY,
  GET_BALANCE_HISTORY,
  GET_ENERGY_STORAGE,
} from './energy_history.methods';
import {
  API_HOUSEHOLDS,
  PART_API_ENERGY_PRODUCTIONS,
  PART_API_ENERGY_USAGES,
  PART_API_BALANCES,
  PART_API_BATTERIES,
  WS_ENTRY,
  WS_QUEUE_ENERGY_PRODUCTION,
  WS_QUEUE_ENERGY_USAGE,
  WS_QUEUE_BALANCE,
  WS_QUEUE_ENERGY_STORAGE,
} from '../../endpoints';
import WebSocketConnection from '../../websocket_handler';
import {
  BALANCE_HISTORY_LENGTH_MILIS,
  ENERGY_PRODUCTION_HISTORY_LENGTH_MILIS,
  ENERGY_USAGE_HISTORY_LENGTH_MILIS,
  HISTORY_TIME_STEP,
} from '../../constants';

const MAX_DATA_LENGTH = ENERGY_USAGE_HISTORY_LENGTH_MILIS / HISTORY_TIME_STEP;
const DATA_LENGTH_CUTOFF = Math.floor(MAX_DATA_LENGTH * 1.1);

export default {
  state: {
    energy_usages: [],
    energy_productions: [],
    balances: [],
    energy_storage: [],
    received: 0,
    connection: null,
  },


  mutations: {
    [SET_CONNECTION]: (state, connection) => {
      state.connection = connection;
    },

    [ADD_ENERGY_USAGE]: (state, datapoint) => {
      state.energy_usages.push(datapoint);
      state.received += 1;
      if (state.energy_usages.length > DATA_LENGTH_CUTOFF) {
        state.energy_usages = state.energy_usages.slice(-MAX_DATA_LENGTH);
      }
    },

    [ADD_ENERGY_PRODUCTION]: (state, datapoint) => {
      state.energy_productions.push(datapoint);
      state.received += 1;
      if (state.energy_productions.length > DATA_LENGTH_CUTOFF) {
        state.energy_productions = state.energy_productions.slice(-MAX_DATA_LENGTH);
      }
    },

    [ADD_BALANCE]: (state, datapoint) => {
      state.balances.push(datapoint);
      state.received += 1;
      if (state.balances.length > DATA_LENGTH_CUTOFF) {
        state.balances = state.balances.slice(-MAX_DATA_LENGTH);
      }
    },

    [SET_ENERGY_USAGES]: (state, datapoints) => {
      state.energy_usages = datapoints;
      state.received = 0;
    },

    [SET_ENERGY_PRODUCTIONS]: (state, datapoints) => {
      state.energy_productions = datapoints;
      state.received = 0;
    },

    [SET_BALANCES]: (state, datapoints) => {
      state.balances = datapoints;
      state.received = 0;
    },

    [SET_ENERGY_STORAGE]: (state, datapoints) => {
      state.energy_storage = datapoints.sort((a, b) => b.totalPowerStorage - a.totalPowerStorage);
    },
  },


  getters: {
    [ENERGY_USAGES]: state => state.energy_usages,

    [ENERGY_PRODUCTIONS]: state => state.energy_productions,

    [BALANCES]: state => state.balances.map(balance => ({
      x: balance.x,
      y: balance.y / 100,
    })),

    [ENERGY_STORAGE]: state => state.energy_storage,

    [DATAPOINTS_RECEIVED]: state => state.received,
  },


  actions: {
    [MAKE_CONNECTION]: (context, onConnectionLostHandler) => {
      const connection = new WebSocketConnection(
        WS_ENTRY,
        () => {},
        onConnectionLostHandler,
      );
      context.commit(SET_CONNECTION, connection);
    },

    [SUBSCRIBE]: (context, householdId) => {
      context.state.connection.registerHousehold(householdId);
      context.state.connection.subscribe(
        WS_QUEUE_ENERGY_USAGE,
        response => context.commit(ADD_ENERGY_USAGE, JSON.parse(response.body)),
        () => context.dispatch(GET_ENERGY_USAGE_HISTORY, householdId),
      );

      context.state.connection.subscribe(
        WS_QUEUE_ENERGY_PRODUCTION,
        response => context.commit(ADD_ENERGY_PRODUCTION, JSON.parse(response.body)),
        () => context.dispatch(GET_ENERGY_PRODUCTION_HISTORY, householdId),
      );

      context.state.connection.subscribe(
        WS_QUEUE_BALANCE,
        response => context.commit(ADD_BALANCE, JSON.parse(response.body)),
        () => context.dispatch(GET_BALANCE_HISTORY, householdId),
      );

      context.state.connection.subscribe(
        WS_QUEUE_ENERGY_STORAGE,
        response => context.commit(SET_ENERGY_STORAGE, JSON.parse(response.body)),
        () => context.dispatch(GET_ENERGY_STORAGE, householdId),
      );
    },

    [UNSUBSCRIBE]: (context) => {
      context.state.connection.unsubscribeAll();
      context.state.connection.deregisterHousehold();
    },

    [GET_ENERGY_USAGE_HISTORY]: (context, householdId) => {
      const startTimeMilis = new Date().getTime() - ENERGY_PRODUCTION_HISTORY_LENGTH_MILIS;
      Axios.get(
        `${API_HOUSEHOLDS}/${householdId}/${PART_API_ENERGY_USAGES}/${startTimeMilis}`,
      ).then((response) => {
        context.commit(SET_ENERGY_USAGES, response.data.map(datapoint => ({
          x: new Date(datapoint.timestamp).getTime(),
          y: datapoint.value,
        })));
      }).catch(response => console.log(response));
    },

    [GET_ENERGY_PRODUCTION_HISTORY]: (context, householdId) => {
      const startTimeMilis = new Date().getTime() - ENERGY_USAGE_HISTORY_LENGTH_MILIS;
      Axios.get(
        `${API_HOUSEHOLDS}/${householdId}/${PART_API_ENERGY_PRODUCTIONS}/${startTimeMilis}`,
      ).then((response) => {
        context.commit(SET_ENERGY_PRODUCTIONS, response.data.map(datapoint => ({
          x: new Date(datapoint.timestamp).getTime(),
          y: datapoint.value,
        })));
      }).catch(response => console.log(response));
    },

    [GET_BALANCE_HISTORY]: (context, householdId) => {
      const startTimeMilis = new Date().getTime() - BALANCE_HISTORY_LENGTH_MILIS;
      Axios.get(
        `${API_HOUSEHOLDS}/${householdId}/${PART_API_BALANCES}/${startTimeMilis}`,
      ).then((response) => {
        context.commit(SET_BALANCES, response.data.reverse().map(datapoint => ({
          x: new Date(datapoint.timestamp).getTime(),
          y: datapoint.value,
        })));
      }).catch(response => console.log(response));
    },

    [GET_ENERGY_STORAGE]: (context, householdId) => {
      Axios.get(
        `${API_HOUSEHOLDS}/${householdId}/${PART_API_BATTERIES}`,
      ).then((response) => {
        context.commit(SET_ENERGY_STORAGE, response.data);
      }).catch(response => console.log(response));
    },
  },
};
