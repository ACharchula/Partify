package pl.antonic.partify.model.spotify

import java.io.Serializable

class ObjectList<T>(var items: List<T>? = null) : Serializable