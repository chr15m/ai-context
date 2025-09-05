# LLM.js Usage Guide (including OpenRouter)

This document provides a concise, detail-oriented guide to using `LLM.js`, with a special focus on integrating OpenRouter.

## 1. Overview

`LLM.js` is a JavaScript library providing a single, simple interface for interacting with hundreds of popular Large Language Models (LLMs) from various providers like OpenAI, Anthropic, Google, Mistral, Groq, Llamafile, Ollama, Together, DeepSeek, and OpenAI-compatible endpoints like OpenRouter.

## 2. Installation

Install `LLM.js` from NPM:
```bash
npm install @themaximalist/llm.js
```

## 3. API Key Setup

Set API keys as environment variables or pass them directly in options.

**Standard Providers:**
```bash
export OPENAI_API_KEY=your_openai_key
export ANTHROPIC_API_KEY=your_anthropic_key
export MISTRAL_API_KEY=your_mistral_key
export GOOGLE_API_KEY=your_google_key
export GROQ_API_KEY=your_groq_key
export TOGETHER_API_KEY=your_together_key
export PERPLEXITY_API_KEY=your_perplexity_key
export DEEPSEEK_API_KEY=your_deepseek_key
export OPENROUTER_API_KEY=your_openrouter_key
# etc.
```

**For OpenRouter:**
It's recommended to pass the API key directly in the options object:
`apikey: "YOUR_OPENROUTER_API_KEY"` or `apikey: process.env.OPENROUTER_API_KEY`

If you set `service: "openai"` and do not provide an `apikey` in options, `LLM.js` might attempt to use `process.env.OPENAI_API_KEY`.

## 4. Basic Usage

`LLM.js` can be called as an async function for one-off requests or instantiated for chat history.

**One-off Request:**
```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    const response = await LLM("Translate 'hello' to French.", { model: "gpt-4o-mini" });
    console.log(response); // "Bonjour"
}
main();
```

**Chat Instance (with history):**
```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    const llm = new LLM({ model: "claude-3-haiku-20240307" }); // Default model for this instance
    llm.system("You are a helpful assistant.");
    let response = await llm.chat("What's the capital of France?");
    console.log(response); // "Paris"
    response = await llm.chat("What is its population?"); // Uses context
    console.log(response); // Provides population of Paris
    // Access history: llm.messages
}
main();
```

## 5. Core API Options

The `LLM(input, options)` function and `new LLM(options)` constructor accept an options object:

*   `input`: (For function call) `<string>` or `Array` (Message History).
*   `service`: `<string>` LLM provider (e.g., `"openai"`, `"anthropic"`, `"google"`). For OpenRouter, use `"openai"`.
*   `model`: `<string>` Specific model name (e.g., `"gpt-4o-mini"`, `"claude-3-5-sonnet-latest"`). For OpenRouter, use the format `"vendor/model-name"` (e.g., `"google/gemini-2.5-flash-preview-05-20"`).
*   `endpoint`: `<string>` Custom API endpoint. For OpenRouter, use `"https://openrouter.ai/api/v1"`.
*   `apikey`: `<string>` API key for the service. Crucial for OpenRouter.
*   `max_tokens`: `<int>` Max response length.
*   `temperature`: `<float>` (0-2.0) Model creativity.
*   `seed`: `<int>` For deterministic results (supported by some models).
*   `stream`: `<boolean>` (default: `false`) If `true`, returns an async generator.
*   `stream_handler`: `<function>` Callback for stream chunks when `stream: true`. The main function will then return the full response.
*   `schema`: `<object>` JSON schema to guide model output (OpenAI, Llamafile).
*   `tool`: `<object>` OpenAI tool selection for structured output.
*   `parser`: `<function>` Post-processing parser (e.g., `LLM.parsers.json`).
*   `headers`: `<object>` Additional HTTP headers to send with the request (useful for OpenRouter-specific features).

## 6. Message History Format

Used for `input` when it's an array, or internally by `llm.messages`.
Format is OpenAI-compatible:
```javascript
[
    { role: "system", content: "You are a helpful AI." },
    { role: "user", content: "What is the secret codeword?" },
    { role: "assistant", content: "The codeword is 'synergy'." },
    { role: "user", content: "Did I tell you the codeword?" }
]
```
For multimodal input (e.g., images), the `content` can be an array:
```javascript
{
  role: "user",
  content: [
    { type: "text", text: "What is in this image?" },
    {
      type: "image_url",
      image_url: { url: "image_url_here" }
    }
  ]
}
```

## 7. Using OpenRouter

OpenRouter provides access to a wide variety of models through an OpenAI-compatible API.

**Configuration:**
*   `service`: `"openai"`
*   `model`: OpenRouter model string (e.g., `"anthropic/claude-3.5-sonnet"`, `"google/gemini-pro-1.5"`, `"mistralai/mistral-large"`)
*   `endpoint`: `"https://openrouter.ai/api/v1"`
*   `apikey`: Your OpenRouter API key (e.g., `process.env.OPENROUTER_API_KEY` or the key string).
*   `headers`: Optional additional headers for OpenRouter-specific features:
    ```javascript
    headers: {
      "HTTP-Referer": "https://yourwebsite.com", // Optional: helps with rate limits
      "X-Title": "Your App Name", // Optional: identifies your app in OpenRouter logs
      "X-Model-Priority": "quality" // Optional: "speed", "quality", or "cost"
    }
    ```

**Example: Text Prompt with OpenRouter**
```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    const prompt = "Explain the concept of recursion in simple terms.";
    const options = {
        service: "openai", // Use the OpenAI client
        model: "mistralai/mixtral-8x7b-instruct", // An OpenRouter model
        endpoint: "https://openrouter.ai/api/v1",
        apikey: process.env.OPENROUTER_API_KEY, // Your OpenRouter key
        temperature: 0.7,
        max_tokens: 500,
        headers: {
          "HTTP-Referer": "https://yourwebsite.com", // Optional
          "X-Title": "Recursion Explainer" // Optional
        }
    };
    const response = await LLM(prompt, options);
    console.log(response);
}
main();
```

**Example: Multimodal (Image) Prompt with OpenRouter**
This requires a model on OpenRouter that supports vision input (e.g., some Gemini or Claude models).
```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    const messages = [
        {
            role: "user",
            content: [
                { type: "text", text: "What is in this image? Describe it." },
                {
                    type: "image_url",
                    image_url: {
                        url: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg"
                    }
                }
            ]
        }
    ];

    const options = {
        service: "openai",
        model: "google/gemini-2.5-flash-preview-05-20", // OpenRouter vision-capable model
        endpoint: "https://openrouter.ai/api/v1",
        apikey: process.env.OPENROUTER_API_KEY,
        max_tokens: 300
    };

    const response = await LLM(messages, options);
    console.log(response);
}
main();
```

**Creating a Reusable OpenRouter Configuration**
```javascript
const LLM = require("@themaximalist/llm.js");

// Create a reusable OpenRouter configuration
function createOpenRouterLLM(model) {
    return new LLM({
        service: "openai",
        model: model,
        endpoint: "https://openrouter.ai/api/v1",
        apikey: process.env.OPENROUTER_API_KEY,
        headers: {
            "HTTP-Referer": "https://yourwebsite.com",
            "X-Title": "My LLM.js App"
        }
    });
}

async function main() {
    // Create instances for different models
    const claudeLLM = createOpenRouterLLM("anthropic/claude-3-opus-20240229");
    const geminiLLM = createOpenRouterLLM("google/gemini-1.5-pro");
    
    // Use them with the same interface
    const claudeResponse = await claudeLLM.chat("Explain quantum computing.");
    console.log("Claude says:", claudeResponse);
    
    const geminiResponse = await geminiLLM.chat("Explain quantum computing.");
    console.log("Gemini says:", geminiResponse);
}
main();
```

**Using OpenRouter's Model Routing Feature**
```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    // Let OpenRouter choose the best model based on your criteria
    const options = {
        service: "openai",
        model: "openrouter/auto", // Use OpenRouter's auto-routing
        endpoint: "https://openrouter.ai/api/v1",
        apikey: process.env.OPENROUTER_API_KEY,
        headers: {
            "X-Model-Priority": "quality", // "speed", "quality", or "cost"
            "HTTP-Referer": "https://yourwebsite.com",
            "X-Title": "Auto-Routing Example"
        }
    };
    
    const response = await LLM("Explain how nuclear fusion works", options);
    console.log(response);
}
main();
```

## 8. Streaming

Enable streaming by setting `stream: true`. The `LLM` call will return an async generator.

**Streaming with OpenRouter:**
```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    const prompt = "Write a short story about a robot learning to paint.";
    const options = {
        service: "openai",
        model: "anthropic/claude-3-haiku-20240307", // OpenRouter model
        endpoint: "https://openrouter.ai/api/v1",
        apikey: process.env.OPENROUTER_API_KEY,
        stream: true
    };

    const stream = await LLM(prompt, options);
    for await (const chunk of stream) {
        process.stdout.write(chunk);
    }
    process.stdout.write("\n");
}
main();
```

If using `stream_handler`, the `LLM` call returns the full response after the stream completes.
```javascript
const fullResponse = await LLM(prompt, {
    ...options, // from above
    stream: true,
    stream_handler: (chunk) => process.stdout.write(chunk)
});
process.stdout.write("\nCOMPLETED RESPONSE:\n" + fullResponse);
```

## 9. JSON Output

Use the `schema` option for structured JSON output (if supported by the underlying model via OpenRouter and its OpenAI compatibility).
```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    const prompt = "List three primary colors in JSON format.";
    const schema = {
        type: "object",
        properties: {
            colors: { type: "array", items: { type: "string" } }
        },
        required: ["colors"]
    };
    const options = {
        service: "openai",
        model: "openai/gpt-4o-mini", // A model known for good JSON support via OpenRouter
        endpoint: "https://openrouter.ai/api/v1",
        apikey: process.env.OPENROUTER_API_KEY,
        schema: schema,
        // For some models/providers, you might need to explicitly ask for JSON in the prompt
        // or use the `tool` option if the model supports OpenAI tools via OpenRouter.
    };
    const result = await LLM(prompt, options);
    console.log(JSON.stringify(result, null, 2));
    // Expected: { "colors": ["red", "yellow", "blue"] } or similar
}
main();
```
Alternatively, use `LLM.parsers.json` for less strict JSON extraction from text.

## 10. Parsers

`LLM.js` includes built-in parsers:
*   `LLM.parsers.json(content)`: Parses JSON from string, handles markdown code blocks.
*   `LLM.parsers.codeBlock(blockType)(content)`: Extracts content from a markdown code block (e.g., `codeBlock("javascript")`).
*   `LLM.parsers.xml(tag)(content)`: Extracts content from within specified XML tags.

Example with parser:
```javascript
const colors = await LLM("Return primary colors as a JSON array: ['red', 'green', 'blue']", {
  service: "openai",
  model: "anthropic/claude-3-sonnet-20240229", // OpenRouter model
  endpoint: "https://openrouter.ai/api/v1",
  apikey: process.env.OPENROUTER_API_KEY,
  parser: LLM.parsers.json
});
// colors will be the parsed array: ["red", "green", "blue"]
```

## 11. Static Methods

*   `LLM.serviceForModel(model)`: Returns the likely service for a model string.
*   `LLM.modelForService(service)`: Returns the default model for a service.

## 12. Error Handling

When using OpenRouter, it's important to handle errors properly:

```javascript
const LLM = require("@themaximalist/llm.js");

async function main() {
    try {
        const response = await LLM("Generate a response", {
            service: "openai",
            model: "anthropic/claude-3-opus-20240229",
            endpoint: "https://openrouter.ai/api/v1",
            apikey: process.env.OPENROUTER_API_KEY
        });
        console.log(response);
    } catch (error) {
        console.error("Error type:", error.constructor.name);
        console.error("Error message:", error.message);
        
        // OpenRouter might return detailed error information
        if (error.response) {
            console.error("Status:", error.response.status);
            console.error("Data:", error.response.data);
        }
    }
}
main();
```

## 13. Model Selection Tips

When using OpenRouter, you can access models from multiple providers. Here are some tips:

1. **Cost Efficiency**: OpenRouter provides transparent pricing. Check their dashboard for current rates.
2. **Capability Matching**: Choose models based on your specific needs:
   - Text generation: Most models work well
   - Reasoning: Consider `anthropic/claude-3-opus` or `openai/gpt-4o`
   - Vision: `google/gemini-pro-vision`, `anthropic/claude-3-opus` (with vision), or `openai/gpt-4-vision`
   - Code: `openai/gpt-4o` or `anthropic/claude-3-opus`
3. **Fallback Strategy**: Implement fallbacks between models if one fails or is unavailable.
4. **Auto-Routing**: Use `model: "openrouter/auto"` with the `X-Model-Priority` header to let OpenRouter select the best model based on your priority (speed, quality, or cost).

## 14. Browser Usage

When using LLM.js with OpenRouter in a browser environment, you'll need to handle CORS and API key security:

```javascript
// Import from CDN or bundled file
import LLM from 'https://cdn.jsdelivr.net/npm/@themaximalist/llm.js/dist/llm.min.js';

async function callOpenRouter() {
    try {
        // In browser environments, never expose your API key directly in client-side code
        // Use a backend proxy or secure environment variables
        const apiKey = await fetchApiKeySecurely(); // Implement this securely
        
        const response = await LLM("Explain quantum physics", {
            service: "openai",
            model: "anthropic/claude-3-opus-20240229",
            endpoint: "https://openrouter.ai/api/v1",
            apikey: apiKey,
            headers: {
                "HTTP-Referer": window.location.origin
            }
        });
        
        document.getElementById('result').textContent = response;
    } catch (error) {
        console.error("Error:", error);
        document.getElementById('result').textContent = "Error: " + error.message;
    }
}
```

This summary should provide a solid foundation for using `LLM.js` and integrating it with OpenRouter. Always refer to the official OpenRouter documentation for the latest model names and capabilities.
