# GitHub Actions Setup for New Stations of the Cross

This project is configured with GitHub Actions to automatically build and sign your Android application (APK and AAB) when you publish a release or manually trigger the workflow from the "Actions" tab.

## Prerequisites

Before the workflow can sign your app, you need to set up several **GitHub Secrets** in your repository.

### 1. Prepare your Keystore

If you don't have a keystore yet, you can create one using Android Studio or the `keytool` command line.

### 2. Convert Keystore to Base64

GitHub Secrets cannot store binary files directly. You must convert your `.jks` or `.keystore` file to a Base64 string.

**On Windows (PowerShell):**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("your-keystore-file.jks")) | Out-File -FilePath "keystore-base64.txt"
```

**On macOS/Linux:**
```bash
openssl base64 -in your-keystore-file.jks -out keystore-base64.txt
```

Copy the content of `keystore-base64.txt`.

### 3. Add GitHub Secrets

Go to your repository on GitHub:
1.  **Settings** > **Secrets and variables** > **Actions**.
2.  Click **New repository secret** for each of the following:

| Secret Name | Description |
| :--- | :--- |
| `SIGNING_KEY` | The Base64 string of your keystore file (from step 2). |
| `ALIAS` | Your key alias (e.g., `upload-key`). |
| `KEY_STORE_PASSWORD` | The password for your keystore. |
| `KEY_PASSWORD` | The password for your key (often the same as keystore password). |

## How to Trigger a Build

### Option 1: Create a Release (Recommended)
1.  Go to the **Releases** section on GitHub.
2.  Draft a new release and publish it.
3.  The workflow will start automatically, build the signed APK/AAB, and attach them as release assets.

### Option 2: Manual Trigger
1.  Go to the **Actions** tab in your GitHub repository.
2.  Select the **Android CI/CD** workflow on the left.
3.  Click the **Run workflow** dropdown and then **Run workflow**.
4.  Once finished, the signed files will be available as **Artifacts** in the run summary.

## Workflow Details

The workflow is defined in `.github/workflows/android-release.yml`. It uses:
- **JDK 21**: Required for Gradle 9.1.0.
- **r0adkll/sign-android-release**: A trusted action for signing Android artifacts securely.
- **softprops/action-gh-release**: For automatically attaching builds to GitHub Releases.
