# TODO - GHCR Workflow Setup

- [x] Create GitHub Actions workflow at `.github/workflows/publish-ghcr.yml`
- [x] Configure trigger for `push` on `main` and `workflow_dispatch`
- [x] Configure permissions for `contents: read` and `packages: write`
- [x] Add steps: checkout, login to GHCR, metadata extraction, build & push
- [x] Verify workflow file content and update this checklist
