const websocketHost = () => {
  return 'ws://localhost';
  const loc = window.location;
  if (loc.protocol === 'https:') {
    return `wss://${loc.host}`;
  }
  return `ws://${loc.host}`;
};

// API endpoints
export const API_HOUSEHOLDS = 'http://localhost/api/households';
export const PART_API_ENERGY_USAGES = 'energy_usages';
export const PART_API_ENERGY_PRODUCTIONS = 'energy_productions';
export const PART_API_BATTERIES = 'batteries';
export const PART_API_BALANCES = 'balances';

// Websocket endpoints
export const WS_ENTRY = `${websocketHost()}/api/greeting`;
export const WS_REGISTER_HOUSEHOLD = '/app/register';
export const WS_DEREGISTER_HOUSEHOLD = '/app/deregister';
export const WS_QUEUE_ENERGY_USAGE = '/user/queue/energy-usage';
export const WS_QUEUE_ENERGY_PRODUCTION = '/user/queue/energy-production';
export const WS_QUEUE_ENERGY_STORAGE = '/user/queue/energy-storage';
export const WS_QUEUE_BALANCE = '/user/queue/balance';
