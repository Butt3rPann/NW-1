<script setup>
defineProps({
    text: {
        type: String,
        required: true
    },
    bgColor: String,
    textColor: String,
    borderColor: String,
    hover: String,
    className: String,
    disabled: Boolean
})
</script>
 
<template>
    <button :disabled="disabled" :class="`${className} font-rubik font-medium rounded-xl text-lg cursor-pointer px-7 py-3 ${textColor} ${bgColor} ${borderColor} hover:scale-103 transition-scale ease-in-out duration-300 place-content-center w-fit`">
        {{ text }}
    </button>
</template>
 
<style scoped>

</style>