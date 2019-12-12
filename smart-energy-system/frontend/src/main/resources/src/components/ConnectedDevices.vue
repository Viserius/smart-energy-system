<template>
  <div>
    <appliances />
    <Batteries />
    <Generators />
  </div>
</template>

<script>
import Appliances from './ConnectedDevices/Appliances';
import Batteries from './ConnectedDevices/Batteries';
import Generators from './ConnectedDevices/Generators';

export default {
  name: 'ConnectedDevices',
  components: { Generators, Batteries, Appliances },
};
</script>

<style scoped>

</style>
