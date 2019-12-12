<template>
  <b-card
    v-bind:title="`My Household (${household.name})`"
    class="border-0 bg-transparent"
  >
      <div>
        <b-card-group deck>
          <b-card title="Energy usage">
            <energy-usage />
          </b-card>
          <b-card title="Energy production">
            <energy-production />
          </b-card>
        </b-card-group>
      </div>
      <div class="mt-4">
        <b-card-group deck>
          <b-card title="Balance">
            <monthly-costs />
          </b-card>
          <b-card title="Energy storage">
            <energy-storage />
          </b-card>
        </b-card-group>
      </div>
  </b-card>
</template>

<script>
import { mapGetters } from 'vuex';
import EnergyUsage from './HouseholdDashboard/EnergyUsage';
import EnergyProduction from './HouseholdDashboard/EnergyProduction';
import MonthlyCosts from './HouseholdDashboard/MonthlyCosts';
import EnergyStorage from './HouseholdDashboard/EnergyStorage';
import { GET_HOUSEHOLD } from '../store/modules/current_household.methods';

export default {
  name: 'HouseholdDashboard',
  components: {
    EnergyUsage,
    EnergyProduction,
    MonthlyCosts,
    EnergyStorage,
  },
  computed: {
    ...mapGetters({
      household: GET_HOUSEHOLD,
    }),
  },
};
</script>

<style scoped>

</style>
