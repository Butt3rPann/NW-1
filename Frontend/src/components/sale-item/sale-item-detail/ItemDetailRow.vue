<script setup>
defineProps({
  label: String,
  value: [String, Number, null],
  unit: {
    type: String,
    default: ''
  },
  valueClass: {
    type: String,
    default: '',
  },
  unitClass: {
    type: String,
    default: '',
  }
})
</script>
 
<template>
<div>
    <div class="text-[#332A1E] py-3.5 border-b-3 border-[#E5E8F4]">
        <div class="flex items-center justify-between text-xl gap-30">
          <p class="font-medium">{{ label }}</p>
          <p class="text-right">
            <span :class="[value ? 'text-[#332A1E]' : 'text-[#A4A4A3]', valueClass]">{{ value ? value : '-' }}</span>
            <span v-if="value && unit" :class="unitClass">{{ ` ${unit}` }}</span>
          </p>
        </div>
  </div>
</div>
</template>
 
<style scoped>

</style>