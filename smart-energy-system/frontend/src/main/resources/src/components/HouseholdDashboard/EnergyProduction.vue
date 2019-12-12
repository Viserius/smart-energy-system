<template>
  <div id="chart">
    <apexchart ref="energyProduction" type=line height=350
               :options="chartOptions" :series="series" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { ENERGY_PRODUCTIONS } from '../../store/modules/energy_history.methods';
import { ENERGY_PRODUCTION_HISTORY_LENGTH_MILIS } from '../../constants';

export default {
  name: 'EnergyProduction',
  data() {
    return {
      chartOptions: {
        chart: {
          id: 'energy-productions',
        },
        animations: {
          enabled: true,
          easing: 'linear',
          dynamicAnimation: {
            speed: 5000,
          },
        },
        xaxis: {
          type: 'datetime',
          range: ENERGY_PRODUCTION_HISTORY_LENGTH_MILIS,
        },
        yaxis: {
          decimalsInFloat: 3,
          title: {
            text: 'kWh',
          },
        },
        tooltip: {
          x: {
            format: 'HH:mm:ss',
          },
          y: {
            formatter: (value) => `${value.toFixed(3)}kWh`,
          },
        },
      },
      series: [{
        data: [],
      }],
    };
  },
  methods: {
    updateChart() {
      this.series[0].data = this[ENERGY_PRODUCTIONS];
      if (this.$refs.energyProduction !== undefined) {
        this.$refs.energyProduction.updateSeries(this.series);
      }
    },
  },
  computed: {
    ...mapGetters([
      ENERGY_PRODUCTIONS,
    ]),
  },
  watch: {
    [ENERGY_PRODUCTIONS]() {
      this.updateChart();
    },
  },
  mounted() {
    this.updateChart();
  },
};
</script>

<style scoped>

</style>
