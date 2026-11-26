# Online Feedback — DevOps Demonstration

A small Java 17 + Maven application that serves a single-page HTML feedback form. The form demonstrates client-side validation and a confirmation message; it does **not** persist or transmit submissions.

## Requirements
- JDK 17+
- Apache Maven 3.8+
- Git
- Docker Desktop / Docker Engine (for the Docker stage)
- GitHub repository and a Jenkins agent with Java 17 + Maven

## 1. Stop existing local development servers

**Windows PowerShell** — stop processes listening on common development ports (3000, 5000, 8000, 8080, 8081, 8082, 8888):
```powershell
$ports = 3000,5000,8000,8080,8081,8082,8888
Get-NetTCPConnection -State Listen -ErrorAction SilentlyContinue |
  Where-Object { $_.LocalPort -in $ports } |
  Select-Object -ExpandProperty OwningProcess -Unique |
  ForEach-Object { Stop-Process -Id $_ -Force }
```
This force-stops processes on those ports. Close only processes you own; omit ports you need.

**macOS / Linux**:
```bash
for port in 3000 5000 8000 8080 8081 8082 8888; do
  pids=$(lsof -tiTCP:$port -sTCP:LISTEN)
  [ -z "$pids" ] || kill $pids
done
```

## 2. Build and run locally
From this project folder:
```bash
mvn clean package
java -jar target/online-feedback-1.0.0.jar
```
Open http://localhost:8080. Fill the form and click Submit. Stop the app with `Ctrl+C`.
The generated artifact is `target/online-feedback-1.0.0.jar`.

## 3. GitHub feature branch and merge
Create an empty GitHub repository, then replace the URL below with your repository URL:
```bash
git init
git add .
git commit -m "Add online feedback application and DevOps pipeline"
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
git push -u origin main

git switch -c feature/feedback-form
git add .
git commit -m "Add feedback form and CI configuration"
git push -u origin feature/feedback-form
```
Open a Pull Request on GitHub from `feature/feedback-form` into `main`, review it, and merge it. If you already created the first commit on `main`, the feature branch commit can be a small README change or you can create the feature branch before your first commit.

## 4. Jenkins pipeline
1. Ensure Jenkins has a JDK 17 installation and Maven installation configured under **Manage Jenkins → Tools**. Name the Maven installation exactly `Maven` (or change the `tools` name in `Jenkinsfile`).
2. Create a **Pipeline** job. Configure SCM to point to your GitHub repository and select the branch containing `Jenkinsfile`.
3. Choose **Pipeline script from SCM**, Git, enter the repository URL and credentials if private, and set Script Path to `Jenkinsfile`.
4. Click **Build Now** and open Console Output / Stage View. The Maven Build stage should pass and archive the JAR.

If your Jenkins agent is Linux, `sh` is correct. For a Windows agent, replace `sh 'mvn -B clean package'` with `bat 'mvn -B clean package'`.

## 5. GitHub Actions
The workflow is `.github/workflows/maven.yml`. Push the branch to GitHub. Open **Actions → Maven Build**, select the latest run, and verify all steps are green. The workflow runs on pushes to any branch and pull requests targeting `main`.

## 6. Docker
Build after Maven has created the JAR:
```bash
docker build -t online-feedback:1.0 .
docker run -d --name online-feedback -p 8080:8080 online-feedback:1.0
docker ps
```
Open http://localhost:8080 to verify the form. To stop and remove the container:
```bash
docker stop online-feedback
docker rm online-feedback
```
If port 8080 is already in use, use `-p 8085:8080` and open http://localhost:8085.

## 7. Evidence checklist
Capture screenshots for:
- Feedback form displayed in a browser.
- Successful `mvn clean package` terminal output and `target/online-feedback-1.0.0.jar`.
- GitHub feature branch and commit, plus merged Pull Request.
- Jenkins successful pipeline / Stage View and archived artifact.
- GitHub Actions successful Maven Build run.
- Docker image (`docker images`), running container (`docker ps`), and browser form at localhost.

Put the screenshots into a document/PDF and submit it with the project ZIP as required by your instructor.

## Notes
- The form is a front-end demonstration only. It displays a confirmation but does not store feedback or send email.
- Jenkins, GitHub, and Docker actions require your own accounts/services and cannot be completed from this ZIP alone.
