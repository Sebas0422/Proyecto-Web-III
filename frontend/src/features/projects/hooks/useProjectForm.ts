import { useNavigate } from 'react-router-dom'
import { useCreateProjectMutation, useUpdateProjectMutation } from '../services/projectsApi'
import { extractErrorMessage } from '@shared/utils'
import type { CreateProjectRequest, UpdateProjectRequest } from '@shared/types'

export const useProjectForm = (projectId?: string) => {
  const navigate = useNavigate()
  const [createProject, { isLoading: isCreating, error: createError }] = useCreateProjectMutation()
  const [updateProject, { isLoading: isUpdating, error: updateError }] = useUpdateProjectMutation()

  const isEdit = Boolean(projectId)
  const isLoading = isCreating || isUpdating
  const error = createError || updateError

  const handleSubmit = async (values: CreateProjectRequest | UpdateProjectRequest) => {
    try {
      if (isEdit && projectId) {
        await updateProject({ id: projectId, data: values as UpdateProjectRequest }).unwrap()
        navigate(`/projects/${projectId}`)
      } else {
        const result = await createProject(values as CreateProjectRequest).unwrap()
        navigate(`/projects/${result.id}`)
      }
    } catch (err) {
      console.error('Error al guardar proyecto:', err)
    }
  }

  const handleCancel = () => {
    if (isEdit && projectId) {
      navigate(`/projects/${projectId}`)
    } else {
      navigate('/projects')
    }
  }

  const errorMessage = error ? extractErrorMessage(error) : null

  return {
    handleSubmit,
    handleCancel,
    isLoading,
    isEdit,
    error: errorMessage,
  }
}
