
const form = document.getElementById("urlForm");
const originalUrlInput = document.getElementById("originalUrl");
const result = document.getElementById("result");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const originalUrl = originalUrlInput.value.trim();

    result.textContent = "Generating your short URL...";

    try {
        const response = await fetch("http://localhost:8080/api/urls", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                originalUrl: originalUrl
            })
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`HTTP ${response.status}: ${errorMessage}`);
        }

        const data = await response.json();

        const shortUrl =
            `http://localhost:8080/api/urls/short/${data.shortCode}`;

        // Display the generated short URL and copy button
        result.innerHTML = `
            <p class="success-message">
                URL shortened successfully! 🎉
            </p>

            <div class="short-url-container">
                <a
                    class="short-url"
                    href="${shortUrl}"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    ${shortUrl}
                </a>

                <button
                    id="copyBtn"
                    class="copy-btn"
                    type="button"
                >
                    📋 Copy Link
                </button>
            </div>
        `;

        const copyButton = document.getElementById("copyBtn");

        copyButton.addEventListener("click", async () => {
            try {
                await navigator.clipboard.writeText(shortUrl);

                copyButton.textContent = "✓ Copied!";
                copyButton.classList.add("copied");

            } catch (error) {
                console.error("Copy failed:", error);
                copyButton.textContent = "Copy failed!";

            }
        });

    } catch (error) {
        console.error("Error:", error);

        result.textContent =
            "Something went wrong. Check your backend and try again.";
    }
});
