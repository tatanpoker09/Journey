# -*- coding: utf-8 -*-
"""This module contains a template MindMeld application"""
from .root import app
import journey_core_nlp.handlers
from mindmeld.components.nlp import NaturalLanguageProcessor
nlp = NaturalLanguageProcessor('journey_core_nlp')
nlp.build()
__all__ = ['app']