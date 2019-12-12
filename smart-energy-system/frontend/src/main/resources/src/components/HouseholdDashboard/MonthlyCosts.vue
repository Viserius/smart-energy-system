<template>
  <div id="chart">
    <apexchart ref="balance" type=line height=350
               :options="chartOptions" :series="series" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { BALANCES } from '../../store/modules/energy_history.methods';
import { BALANCE_HISTORY_LENGTH_MILIS } from '../../constants';

export default {
  name: 'MonthlyCosts',
  data() {
    return {
      chartOptions: {
        chart: {
          id: 'balance',
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
          range: BALANCE_HISTORY_LENGTH_MILIS,
        },
        yaxis: {
          decimalsInFloat: 2,
          title: {
            text: 'Balance (USD ¢)',
          },
        },
        tooltip: {
          x: {
            format: 'HH:mm:ss',
          },
          y: {
            formatter: (value) => `¢${value.toFixed(2)}`,
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
      this.series[0].data = this[BALANCES];
      if (this.$refs.balance !== undefined) {
        this.$refs.balance.updateSeries(this.series);
      }
    },
  },
  computed: {
    ...mapGetters([
      BALANCES,
    ]),
  },
  watch: {
    [BALANCES]() {
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
