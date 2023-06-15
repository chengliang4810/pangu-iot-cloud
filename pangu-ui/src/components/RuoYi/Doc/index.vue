<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'pangu-cloud-plus-Wiki',
  data() {
    return {
      url: 'https://gitee.com/dromara/pangu-cloud-plus/wikis'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
