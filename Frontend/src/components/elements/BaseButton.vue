<script setup>
const props = defineProps({
  icon: String,
  text: String,
  disabled: Boolean,
  bgColor: {
    type: String,
    default: 'bg-[#FFFFFF]'
  },
  textColor: {
    type: String,
    default: 'text-[#6F879C]'
  },
  borderColor: {
    type: String,
    default: 'border-[#6F879C]'
  }
})
</script>

<template>
    <button
      :disabled="disabled"
      :class="[
	'flex items-center justify-center w-full md:w-fit py-1.5 md:py-2 md:px-3 lg:py-3 lg:px-4 border-2 rounded-md',
        text === 'Save' ? (disabled ? 'bg-[#6F879C]/50 border-transparent' : 'bg-[#4bbd80] border-transparent') : bgColor,
        borderColor,
        disabled ? 'cursor-not-allowed' : 'cursor-pointer'
    ]">
        <img v-if="icon" :src="icon" alt="Icon" class="w-4.5 md:w-5.5 mr-2" />
        <p :class="[textColor, 'font-medium md:font-semibold text-sm md:text-base']">{{ text }}</p>
    </button>
</template>
 
<style scoped>

</style>
