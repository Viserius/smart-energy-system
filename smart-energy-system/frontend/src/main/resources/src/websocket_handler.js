import {WS_DEREGISTER_HOUSEHOLD, WS_REGISTER_HOUSEHOLD} from './endpoints';

const Stomp = require('@stomp/stompjs');

class WebSocketConnection {
  retryTime;
  connection = null;
  url;
  householdId = null;
  subscriptions = [];
  onConnectionLost;
  onConnect;

  constructor(url, onConnect = () => {}, onConnectionLost = () => {}, retryTime = 5000) {
    this.retryTime = retryTime;
    this.url = url;
    this.onConnect = onConnect;
    this.onConnectionLost = onConnectionLost;
    this.makeConnection(this);
  }

  makeConnection(self) {
    self.connection = Stomp.Stomp.client(self.url);
    self.connection.debug = () => {};
    self.connection.reconnected_delay = self.retryTime;
    self.connection.connect({}, () => {
      self.reconnectHandler();
      self.onConnect();
    }, () => {
      setTimeout(me => me.makeConnection(me), self.retryTime, self);
      self.onConnectionLost();
    }, () => {
      setTimeout(me => me.makeConnection(me), self.retryTime, self);
      self.onConnectionLost();
    });
  }

  registerHousehold(householdId) {
    if (householdId !== null) {
      this.householdId = householdId;
      this.connection.send(WS_REGISTER_HOUSEHOLD, {}, householdId);
    }
  }

  deregisterHousehold() {
    if (this.householdId !== null) {
      this.householdId = null;
      this.connection.send(WS_DEREGISTER_HOUSEHOLD);
    }
  }

  subscribe(topic, callback, precall = () => {}, reconnect = false) {
    const idx = this.subscriptions.findIndex(element => element.topic === topic);
    if (reconnect || idx < 0) {
      const subscription = {
        topic,
        callback,
        precall,
        result: this.connection.subscribe(topic, callback),
      };
      if (reconnect) {
        this.subscriptions[idx] = subscription;
      } else {
        this.subscriptions.push(subscription);
      }
      precall();
    }
  }

  unsubscribe(topic) {
    const idx = this.subscriptions.findIndex(element => element.topic === topic);
    if (idx >= 0) {
      this.subscriptions[idx].result.unsubscribe();
      this.subscriptions.splice(idx, 1);
    }
  }

  unsubscribeAll() {
    this.subscriptions.forEach(({ result }) => {
      result.unsubscribe();
    });
    this.subscriptions = [];
  }

  reconnectHandler() {
    this.registerHousehold(this.householdId);
    this.subscriptions.forEach(({ topic, callback, precall }) => {
      this.subscribe(topic, callback, precall, true);
    });
  }
}

export default WebSocketConnection;
