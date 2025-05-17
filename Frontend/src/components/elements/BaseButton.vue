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
        'flex items-center justify-center py-3 px-4 border-2 w-fit rounded-md cursor-pointer',
        text === 'Save' ? (disabled ? 'bg-[#6F879C]/50 cursor-not-allowed border-borderColor border-transparent' : 'bg-[#4bbd80] border-transparent') : bgColor,
        borderColor
    ]">
        <img v-if="icon" :src="icon" alt="Icon" class="w-5.5 mr-2" />
        <p :class="[textColor, 'font-semibold text-base']">{{ text }}</p>
    </button>
</template>
 
<style scoped>

</style>