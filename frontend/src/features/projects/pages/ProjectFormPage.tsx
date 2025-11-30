import React from 'react'
import { Formik, Form } from 'formik'
import type { FormikHelpers } from 'formik'
import * as Yup from 'yup'
import { useNavigate } from 'react-router-dom'
import Button from '@shared/components/Button'
import Input from '@shared/components/Input'

interface ProjectValues {
  title: string
  description: string
  budget: number | ''
  startDate: string
  endDate: string
}

const ProjectSchema = Yup.object().shape({
  title: Yup.string().required('El título es requerido'),
  description: Yup.string().required('La descripción es requerida'),
  budget: Yup.number().min(0, 'El presupuesto debe ser positivo').nullable(),
  startDate: Yup.string().required('La fecha de inicio es requerida'),
  endDate: Yup.string().required('La fecha de fin es requerida'),
})

const ProjectFormPage: React.FC = () => {
  const navigate = useNavigate()

  const handleSubmit = async (
    values: ProjectValues,
    { setSubmitting }: FormikHelpers<ProjectValues>,
  ) => {
    try {
      // TODO: Wire this form to a backend service or RTK Query mutation.
      // For now we just log the values and navigate to the dashboard.
      console.log('Proyecto submit', values)
      navigate('/dashboard')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 p-4">
      <div className="w-full max-w-2xl">
        <div className="text-center mb-8">
          <h2 className="text-4xl font-bold text-gray-900 mb-2">Nuevo Proyecto</h2>
          <p className="text-gray-600">Completa la información del proyecto</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
          <Formik
            initialValues={{ title: '', description: '', budget: '', startDate: '', endDate: '' }}
            validationSchema={ProjectSchema}
            onSubmit={handleSubmit}
          >
            {({ isSubmitting }) => (
              <Form className="space-y-5">
                <Input name="title" label="Título" type="text" placeholder="Ej: Proyecto de sitio web" />

                <Input
                  name="description"
                  label="Descripción"
                  type="text"
                  placeholder="Descripción breve del proyecto"
                />

                <Input name="budget" label="Presupuesto" type="number" placeholder="0" />

                <div className="grid grid-cols-2 gap-4">
                  <Input name="startDate" label="Fecha inicio" type="date" placeholder="" />
                  <Input name="endDate" label="Fecha fin" type="date" placeholder="" />
                </div>

                <Button type="submit" variant="primary" size="lg" isLoading={isSubmitting} className="w-full">
                  Guardar proyecto
                </Button>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    </div>
  )
}

export default ProjectFormPage
