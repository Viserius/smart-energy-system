<template>
  <b-badge variant="light">{{ received }}</b-badge>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { DATAPOINTS_RECEIVED, MAKE_CONNECTION } from '../store/modules/energy_history.methods';
import { GET_HOUSEHOLD } from '../store/modules/current_household.methods';

export default {
  name: 'ContinuousDataGathering',
  methods: {
    ...mapActions([
      MAKE_CONNECTION,
    ]),
  },
  computed: {
    ...mapGetters({
      received: DATAPOINTS_RECEIVED,
      household: GET_HOUSEHOLD,
    }),
  },
  mounted() {
    this[MAKE_CONNECTION](() => {
      this.$bus.emit('backendOffline');
    });
  },
};
</script>
