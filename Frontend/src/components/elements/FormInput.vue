<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
    label: {
        type: String,
        required: true
    },
    required: Boolean,
    inputType: {
        type: String,
        required: true
    },
    field: {
        type: String,
        required: true
    },
    placeholder: String,
    className: String,
    maxlength: Number,
    minlength: Number,
    invalidMessage: String,
    min: Number,
    max: Number,
    step: Number,
    trim: {
        type: Boolean,
        default: true
    },
    inputmode: String,
    pattern: String,
    limitLength: Number,
    readonly: { 
        type: Boolean,
        default: false
    },
    labelSize: {
        type: String,
        default: 'text-sm sm:text-base lg:text-lg'
    },
    inputSize: {
        type: String,
        default: 'text-sm md:text-base'
    },
    propsInvalid: {
        type: Boolean
    }
})

const currentType = ref(props.inputType)

function togglePassword() {
    showPassword.value = !showPassword.value
    if (currentType.value === 'password') {
      currentType.value = 'text'
    } else {
      currentType.value = 'password'
    }
}

const inputValue = defineModel()
const inputRef = ref(null)
const isValid = ref(true)

const emit = defineEmits(['disabledButton'])

function handleBlur() {
    if (inputValue.value === '' && props.required) {
        isValid.value = false
    } else if (inputValue.value.length > props.maxlength || (props.min && inputValue.value.length < props.min)) {
        isValid.value = false
    } else if (props.pattern) {
        isValid.value = new RegExp(props.pattern).test(inputValue.value)
    } else if (inputRef.value && inputValue.value !== '') {
        isValid.value = inputRef.value.checkValidity() 
    } else {
        isValid.value = true
    }

    emit('disabledButton', props.field, !isValid.value)
}

const charCount = computed(() => {
  if (typeof inputValue.value !== 'string') return ''
  return props.maxlength
    ? `${inputValue.value.length} / ${props.maxlength}`
    : ''
})

const showPassword = ref(false)

</script>

<template>
    <div class="font-rubik flex flex-col w-full">
        <label class="text-[#332A1E] font-medium mb-1" :class="labelSize">{{ label }}
            <span v-if="required === true" class="text-red-700">*</span>
        </label>
        <textarea v-if="currentType === 'textarea'" v-model.trim="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur" :readonly="readonly"
            :class="[
                `${className} appearance-none py-2 md:py-3 w-full ${inputSize} text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-3 md:px-5 mt-1 min-h-[6rem] selection:bg-[#2684FF]/30 focus:outline-none`,
                { 'border-red-400' : !isValid },
                readonly ? 'bg-gray-100' : 'bg-white focus:ring-2 focus:ring-[#2684FF]'
            ]"/>
        <div v-else-if="trim" class="relative">
            <input :type="currentType" :inputmode="inputmode" :required="required" v-model.trim="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur" :maxlength="limitLength" :minlength="minlength" :min="min" :max="max" :step="step" :readonly="readonly"
            :class="[
                `${className} h-[2rem] md:h-[2.5rem] lg:h-[2.75rem] appearance-none w-full ${inputSize} text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-3 md:px-5 mt-1 selection:bg-[#2684FF]/30 focus:outline-none`,
                { 'border-red-400' : !isValid },
                readonly ? 'bg-gray-100' : 'bg-white focus:ring-2 focus:ring-[#2684FF]'
            ]"/>
            <button v-if="inputType === 'password'" @click="togglePassword" class="absolute right-3 top-1/2 -translate-y-1/3 text-gray-500 cursor-pointer">
                <svg v-if="!showPassword" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36" class="w-4 h-4 md:w-5 md:h-5">
                    <path fill="currentColor" d="M25.19 20.4a6.8 6.8 0 0 0 .43-2.4a6.86 6.86 0 0 0-6.86-6.86a6.8 6.8 0 0 0-2.37.43L18 13.23a5 5 0 0 1 .74-.06A4.87 4.87 0 0 1 23.62 18a5 5 0 0 1-.06.74Z" class="clr-i-outline clr-i-outline-path-1" />
                    <path fill="currentColor" d="M34.29 17.53c-3.37-6.23-9.28-10-15.82-10a16.8 16.8 0 0 0-5.24.85L14.84 10a14.8 14.8 0 0 1 3.63-.47c5.63 0 10.75 3.14 13.8 8.43a17.8 17.8 0 0 1-4.37 5.1l1.42 1.42a19.9 19.9 0 0 0 5-6l.26-.48Z" class="clr-i-outline clr-i-outline-path-2" />
                    <path fill="currentColor" d="m4.87 5.78l4.46 4.46a19.5 19.5 0 0 0-6.69 7.29l-.26.47l.26.48c3.37 6.23 9.28 10 15.82 10a16.9 16.9 0 0 0 7.37-1.69l5 5l1.75-1.5l-26-26Zm9.75 9.75l6.65 6.65a4.8 4.8 0 0 1-2.5.72A4.87 4.87 0 0 1 13.9 18a4.8 4.8 0 0 1 .72-2.47m-1.45-1.45a6.85 6.85 0 0 0 9.55 9.55l1.6 1.6a14.9 14.9 0 0 1-5.86 1.2c-5.63 0-10.75-3.14-13.8-8.43a17.3 17.3 0 0 1 6.12-6.3Z" class="clr-i-outline clr-i-outline-path-3" />
                    <path fill="none" d="M0 0h36v36H0z" />
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36" class="w-4 h-4 md:w-5 md:h-5">
                    <path fill="currentColor" d="M33.62 17.53c-3.37-6.23-9.28-10-15.82-10S5.34 11.3 2 17.53l-.28.47l.26.48c3.37 6.23 9.28 10 15.82 10s12.46-3.72 15.82-10l.26-.48Zm-15.82 8.9C12.17 26.43 7 23.29 4 18c3-5.29 8.17-8.43 13.8-8.43S28.54 12.72 31.59 18c-3.05 5.29-8.17 8.43-13.79 8.43" class="clr-i-outline clr-i-outline-path-1" />
                    <path fill="currentColor" d="M18.09 11.17A6.86 6.86 0 1 0 25 18a6.86 6.86 0 0 0-6.91-6.83m0 11.72A4.86 4.86 0 1 1 23 18a4.87 4.87 0 0 1-4.91 4.89" class="clr-i-outline clr-i-outline-path-2" />
                    <path fill="none" d="M0 0h36v36H0z" />
                </svg>
            </button>
        </div>
        <div v-else class="relative">
            <input  :type="currentType" :required="required" :inputmode="inputmode" v-model="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur" :min="min" :max="max" :step="step" :maxlength="limitLength" :minlength="minlength" :readonly="readonly"
            :class="[
                `${className} h-[2rem] md:h-[2.5rem] lg:h-[2.75rem] appearance-none w-full ${inputSize} text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-3 md:px-5 mt-1 selection:bg-[#2684FF]/30 focus:outline-noneq`,
                { 'border-red-400' : !isValid },
                readonly ? 'bg-gray-100' : 'bg-white focus:ring-2 focus:ring-[#2684FF]'
            ]"/>
            <button v-if="inputType === 'password'" @click="togglePassword" class="absolute right-3 top-1/2 -translate-y-1/3 text-gray-500 cursor-pointer">
                <svg v-if="!showPassword" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36" class="w-4 h-4 md:w-5 md:h-5">
                    <path fill="currentColor" d="M25.19 20.4a6.8 6.8 0 0 0 .43-2.4a6.86 6.86 0 0 0-6.86-6.86a6.8 6.8 0 0 0-2.37.43L18 13.23a5 5 0 0 1 .74-.06A4.87 4.87 0 0 1 23.62 18a5 5 0 0 1-.06.74Z" class="clr-i-outline clr-i-outline-path-1" />
                    <path fill="currentColor" d="M34.29 17.53c-3.37-6.23-9.28-10-15.82-10a16.8 16.8 0 0 0-5.24.85L14.84 10a14.8 14.8 0 0 1 3.63-.47c5.63 0 10.75 3.14 13.8 8.43a17.8 17.8 0 0 1-4.37 5.1l1.42 1.42a19.9 19.9 0 0 0 5-6l.26-.48Z" class="clr-i-outline clr-i-outline-path-2" />
                    <path fill="currentColor" d="m4.87 5.78l4.46 4.46a19.5 19.5 0 0 0-6.69 7.29l-.26.47l.26.48c3.37 6.23 9.28 10 15.82 10a16.9 16.9 0 0 0 7.37-1.69l5 5l1.75-1.5l-26-26Zm9.75 9.75l6.65 6.65a4.8 4.8 0 0 1-2.5.72A4.87 4.87 0 0 1 13.9 18a4.8 4.8 0 0 1 .72-2.47m-1.45-1.45a6.85 6.85 0 0 0 9.55 9.55l1.6 1.6a14.9 14.9 0 0 1-5.86 1.2c-5.63 0-10.75-3.14-13.8-8.43a17.3 17.3 0 0 1 6.12-6.3Z" class="clr-i-outline clr-i-outline-path-3" />
                    <path fill="none" d="M0 0h36v36H0z" />
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36" class="w-4 h-4 md:w-5 md:h-5">
                    <path fill="currentColor" d="M33.62 17.53c-3.37-6.23-9.28-10-15.82-10S5.34 11.3 2 17.53l-.28.47l.26.48c3.37 6.23 9.28 10 15.82 10s12.46-3.72 15.82-10l.26-.48Zm-15.82 8.9C12.17 26.43 7 23.29 4 18c3-5.29 8.17-8.43 13.8-8.43S28.54 12.72 31.59 18c-3.05 5.29-8.17 8.43-13.79 8.43" class="clr-i-outline clr-i-outline-path-1" />
                    <path fill="currentColor" d="M18.09 11.17A6.86 6.86 0 1 0 25 18a6.86 6.86 0 0 0-6.91-6.83m0 11.72A4.86 4.86 0 1 1 23 18a4.87 4.87 0 0 1-4.91 4.89" class="clr-i-outline clr-i-outline-path-2" />
                    <path fill="none" d="M0 0h36v36H0z" />
                </svg>
            </button>
        </div>

	    <div class="flex flex-col sm:flex-row sm:justify-between items-start mt-1 sm:mt-1.5 gap-1 sm:gap-2">
            <p v-if="!isValid || props.propsInvalid" class="itbms-message text-xs text-red-400">{{ invalidMessage }}</p>
            <p :class="['text-xs text-gray-500 ml-auto whitespace-nowrap', {'text-red-400' : inputValue.length > props.maxlength}]" v-if="typeof inputValue === 'string'">{{ charCount }}</p>
        </div>
    </div>
</template>

<style scoped></style>
