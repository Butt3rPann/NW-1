<script setup>
defineProps({
    text: {
        type: String,
        required: true
    },
    bgColor: String,
    textColor: String,
    hover: String
})
</script>
 
<template>
    <button :class="`font-rubik font-medium rounded-xl text-lg cursor-pointer px-7 py-3 ${textColor} ${bgColor} hover:${hover} place-content-center w-fit`">
        {{ text }}
    </button>
</template>
 
<style scoped>

</style>