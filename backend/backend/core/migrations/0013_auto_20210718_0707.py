# Generated by Django 3.2.4 on 2021-07-18 07:07

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0012_alter_itap_seed'),
    ]

    operations = [
        migrations.AlterField(
            model_name='itap',
            name='activation_key',
            field=models.CharField(blank=True, max_length=8, null=True),
        ),
        migrations.AlterField(
            model_name='itap',
            name='seed',
            field=models.CharField(default=uuid.UUID('9f8bec9d-ec32-4207-bf70-e63b3a6c1bba'), max_length=36),
        ),
    ]